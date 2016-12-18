//
//  WhiteNoise.swift
//  White Noise IOS
//
//  Created by Kevin Guthrie on 12/12/16.
//  Copyright © 2016 RookAndPawn. All rights reserved.
//

import Foundation
import AVFoundation
import MediaPlayer

public class WhiteNoise {

	public static let instance = WhiteNoise()

	private let commandQueue : DispatchQueue
		= DispatchQueue(label: "WhiteNoiseCommandQueue")

	var playerControls = MPRemoteCommandCenter.shared()
	var playerInfo = MPNowPlayingInfoCenter.default()

	var playerStatus = AVPlayerTimeControlStatus.paused

	let source: AudioSource
	let session: AVAudioSession = AVAudioSession.sharedInstance()

	public var playStateChangeCallback : ((_ isPlaying : Bool) -> Void)?

	init() {

		source = AudioSource()

		playerControls.playCommand.addTarget(handler: {
				(MPRemoteCommandEvent) ->  MPRemoteCommandHandlerStatus in
					_ = self.play()
					return MPRemoteCommandHandlerStatus.success
				})

		playerControls.pauseCommand.addTarget(handler: {
				(MPRemoteCommandEvent) ->  MPRemoteCommandHandlerStatus in
					self.pause()
					return MPRemoteCommandHandlerStatus.success

				})

		playerControls.togglePlayPauseCommand.addTarget(handler: {
				(e:MPRemoteCommandEvent) ->  MPRemoteCommandHandlerStatus in
					if (self.isPlaying) {
						self.pause()
					}
					else {
						_ = self.play()
					}
					return MPRemoteCommandHandlerStatus.success

				})
	}

	public func pause() {
		playerStatus = AVPlayerTimeControlStatus.paused
		self.playStateChangeCallback?(false)
		
		commandQueue.async {
			self.source.pause()

			self.playerInfo.nowPlayingInfo = [
				MPMediaItemPropertyTitle:"White Noise",
				MPMediaItemPropertyArtist:"",
				MPNowPlayingInfoPropertyPlaybackRate:0
			]
		}

	}

	public var isPlaying : Bool { get { return source.isPlaying() } }

	public func play() {
		playerStatus = AVPlayerTimeControlStatus.playing
		self.playStateChangeCallback?(true)

		commandQueue.async {

			try! AVAudioSession.sharedInstance().setCategory(
					AVAudioSessionCategoryPlayback)

			self.source.play()

			self.playerInfo.nowPlayingInfo = [
				MPMediaItemPropertyTitle:"White Noise",
				MPMediaItemPropertyArtist:"",
				MPNowPlayingInfoPropertyPlaybackRate:1
			]
		}
	}

}
