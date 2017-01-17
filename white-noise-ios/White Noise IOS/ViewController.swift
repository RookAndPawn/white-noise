//
//  ViewController.swift
//  White Noise IOS
//
//  Created by Kevin Guthrie on 12/12/16.
//  Copyright © 2016 RookAndPawn. All rights reserved.
//

import UIKit
import MediaPlayer
import AVKit
import Guava
import WhiteNoiseCore

class ViewController: UIViewController, RPUIView, RPUIHasText {

	var javaPlayButtonRef : RPUIHasClickHandlers!
	var javaPauseButtonRef : RPUIHasClickHandlers!
	var javaPlayPauseButtonRef : RPUIHasClickHandlers!


	var audioPresenter : RPAVAudioPresenter!
	var audioView : IosAudioView!
	var presenter : RPUIPresenter!
	var eventBus : ComGoogleCommonEventbusAsyncEventBus!
	var playerInfo : MPNowPlayingInfoCenter!
	var playerControls : MPRemoteCommandCenter!

	// MARK

	@IBOutlet weak var btnPlayPause: UIButton!

	override func viewDidLoad() {
		super.viewDidLoad()
		// Do any additional setup after loading the view, typically from a nib.

		let executor : JavaUtilConcurrentExecutor =
				JavaUtilConcurrentExecutors.newSingleThreadExecutor()

		playerInfo = MPNowPlayingInfoCenter.default()
		playerControls = MPRemoteCommandCenter.shared()

		eventBus = ComGoogleCommonEventbusAsyncEventBus(
			    javaUtilConcurrentExecutor: executor)
		
		audioView = IosAudioView()
		audioPresenter = RPAVAudioPresenter(rpavAudioView: audioView)

		audioPresenter.bind()

		javaPlayButtonRef = RPUIHasClickHandlers()
		javaPauseButtonRef = RPUIHasClickHandlers()
		javaPlayPauseButtonRef = RPUIHasClickHandlers()
		
		presenter = RPUIPresenter(rpuiView: self
				, with: audioPresenter
				, with: eventBus)

		presenter.bind()

		// Add callbacks to the remote control event for play/pause

		playerControls.togglePlayPauseCommand.addTarget(handler: {
			(e:MPRemoteCommandEvent) ->  MPRemoteCommandHandlerStatus in
			self.javaPlayPauseButtonRef.click()
			return MPRemoteCommandHandlerStatus.success

		})

		playerControls.playCommand.addTarget(handler: {
			(e:MPRemoteCommandEvent) ->  MPRemoteCommandHandlerStatus in
			self.javaPlayButtonRef.click()
			return MPRemoteCommandHandlerStatus.success

		})

		playerControls.pauseCommand.addTarget(handler: {
			(e:MPRemoteCommandEvent) ->  MPRemoteCommandHandlerStatus in
			self.javaPauseButtonRef.click()
			return MPRemoteCommandHandlerStatus.success

		})

	}

	override func didReceiveMemoryWarning() {
		super.didReceiveMemoryWarning()
		// Dispose of any resources that can be recreated.
	}

	func getText() -> String! {
		return btnPlayPause.currentTitle
	}

	func setTextWith(_ text: String!) {
		DispatchQueue.main.async {
			self.btnPlayPause.setTitle(text, for: .normal)
		}
	}

	func getPlayPauseButtonText() -> RPUIHasText! {
		return self
	}

	func getPlayPauseButton() -> RPUIHasClickHandlers! {
		return javaPlayPauseButtonRef
	}


	func getPlayButton() -> RPUIHasClickHandlers! {
		return javaPlayButtonRef
	}

	func getPauseButton() -> RPUIHasClickHandlers! {
		return javaPauseButtonRef
	}


	func setNowPlayingTextWith(_ mainText: String!, with subText: String!) {
		playerInfo.nowPlayingInfo = [
			MPMediaItemPropertyTitle:mainText,
			MPMediaItemPropertyArtist:subText
		]
	}

	@IBAction func togglePlayPause(_ sender: UIButton) {
		javaPlayPauseButtonRef.click()
	}
}

