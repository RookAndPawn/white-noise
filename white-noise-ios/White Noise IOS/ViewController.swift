//
//  ViewController.swift
//  White Noise IOS
//
//  Created by Kevin Guthrie on 12/12/16.
//  Copyright © 2016 RookAndPawn. All rights reserved.
//

import UIKit
import WhiteNoiseCore
import MediaPlayer
import AVKit

class ViewController: UIViewController {

	var whiteNoise : WhiteNoise!

	// MARK

	@IBOutlet weak var btnPlayPause: UIButton!


	override func viewDidLoad() {
		super.viewDidLoad()
		// Do any additional setup after loading the view, typically from a nib.

		whiteNoise = WhiteNoise.instance

		whiteNoise.playStateChangeCallback = {(isPlaying : Bool) in
			self.btnPlayPause.setTitle(isPlaying ? "Pause" : "Play"
					, for: UIControlState())
		}
	}
	override func didReceiveMemoryWarning() {
		super.didReceiveMemoryWarning()
		// Dispose of any resources that can be recreated.
	}

	@IBAction func togglePlayPause(_ sender: UIButton) {
		if whiteNoise.isPlaying {
			whiteNoise.pause()
		} else {
			whiteNoise.play()
		}
	}
}

