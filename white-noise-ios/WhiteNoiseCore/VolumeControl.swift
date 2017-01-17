//
//  VolumeControl.swift
//  White Noise IOS
//
//  Created by Kevin Guthrie on 1/7/17.
//  Copyright © 2017 RookAndPawn. All rights reserved.
//

import UIKit


/**
 * Volume control via mixer node
 */
class VolumeControl: NSObject, RPAVHasFloat {

	let mixer : AVAudioMixerNode

	init(mixer : AVAudioMixerNode) {
		self.mixer = mixer;
	}

	func getValue() -> jfloat {
		return mixer.outputVolume
	}

	func setValueWith(_ value: jfloat) {
		mixer.outputVolume = value
	}

}
