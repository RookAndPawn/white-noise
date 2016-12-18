//
//  AudioUnit.swift
//  white-noise-ios
//
//  Created by Kevin Guthrie on 12/7/16.
//  Copyright © 2016 RookAndPawn. All rights reserved.
//

import Foundation
import AVFoundation
import JRE

private let BUFFER_COUNT = 8
private let SAMPLE_RATE = 44100.0
private let BUFFER_CAPACITY : AVAudioFrameCount = 2048

public class AudioSource {

	let audioFormat: AVAudioFormat
			= AVAudioFormat(
					standardFormatWithSampleRate: SAMPLE_RATE
					, channels: 1)

	let audioEngine : AVAudioEngine = AVAudioEngine()
	let audioPlayer : AVAudioPlayerNode = AVAudioPlayerNode()
	let mixer : AVAudioMixerNode

	let audioGenerator : ComRookandpawnWhitenoiseAudioGenerator
			= ComRookandpawnWhitenoiseWhiteNoiseGenerator(
					double: SAMPLE_RATE)

	// A circular queue of audio buffers.
	private var audioBuffers: [AVAudioPCMBuffer] = [AVAudioPCMBuffer]()

	// The index of the next buffer to fill.
	private var bufferIndex: Int = 0

	// The dispatch queue to render audio samples.
	private let audioQueue: DispatchQueue
			= DispatchQueue(label: "WhiteNoiseBufferQueue")

	// A semaphore to gate the number of buffers processed.
	private let audioSemaphore: DispatchSemaphore
			= DispatchSemaphore(value: BUFFER_COUNT)

	private var isAudioPlaying : Bool = false

	init() {

		// Create a pool of audio buffers.
		for _ in 0 ..< BUFFER_COUNT {
			let audioBuffer = AVAudioPCMBuffer(
					pcmFormat: audioFormat, frameCapacity: BUFFER_CAPACITY)
			audioBuffers.append(audioBuffer)
		}

		// Attach and connect the player node.
		audioEngine.attach(audioPlayer)
		audioEngine.connect(audioPlayer
				, to: audioEngine.mainMixerNode
				, format: audioFormat)

		mixer = audioEngine.mainMixerNode

		mixer.outputVolume = 0
	}

	func fadeIn() {
		self.mixer.outputVolume = 0.0

		for _ in 0 ..< 30 {
			usleep(1333)
			self.mixer.outputVolume += 0.03333
		}

		self.mixer.outputVolume = 1.0
	}

	func fadeOut() {

		self.mixer.outputVolume = 1.0

		for _ in 0 ..< 30 {
			usleep(1333)
			self.mixer.outputVolume -= 0.03333
		}

		self.mixer.outputVolume = 0.0
	}

	public func isPlaying() -> Bool {
		return isAudioPlaying
	}

	public func pause() {
		self.fadeOut()
		self.audioPlayer.pause()
		self.audioEngine.stop()
		isAudioPlaying = false
	}

	public func play() {

		try! self.audioEngine.start()

		isAudioPlaying = true

		self.audioQueue.async {
			while self.audioPlayer.isPlaying {
				// Wait for a buffer to become available.
				_ = self.audioSemaphore.wait(
					timeout: DispatchTime.distantFuture)

				// Fill the buffer with new samples.
				let audioBuffer = self.audioBuffers[self.bufferIndex]
				let momoChannel = audioBuffer.floatChannelData?[0]
				for sampleIndex in 0 ..< Int(BUFFER_CAPACITY) {
					momoChannel?[sampleIndex]
						= self.audioGenerator.getNextSample()
				}
				audioBuffer.frameLength = BUFFER_CAPACITY

				// Schedule the buffer for playback and release it for reuse 
				self.audioPlayer.scheduleBuffer(audioBuffer) {
					self.audioSemaphore.signal()
					return
				}

				self.bufferIndex = (self.bufferIndex + 1)
						% self.audioBuffers.count
			}
		}

		self.audioPlayer.play()
		self.fadeIn()
	}

}
