//
//  J2ObjcPcmBuffer.m
//  White Noise IOS
//
//  Created by Kevin Guthrie on 12/31/16.
//  Copyright © 2016 RookAndPawn. All rights reserved.
//

#import "J2ObjcPcmBuffer.h"

@implementation J2ObjcPcmBuffer

@synthesize audioBufferList = _audioBufferList;
@synthesize mutableAudioBufferList = _mutableAudioBufferList;

- (nonnull instancetype)initWithPCMFormat:(nonnull AVAudioFormat *)format
		frameCapacity:(AVAudioFrameCount)frameCapacity
{
	self = [super initWithPCMFormat:format frameCapacity:frameCapacity];

	if (self) {
		_channelBuffer = [IOSFloatArray arrayWithLength:frameCapacity];

		_buffer.mDataByteSize = frameCapacity;
		_buffer.mData = [ _channelBuffer floatRefAtIndex: 0 ];
		_buffer.mNumberChannels = 1;

		_bufferList.mBuffers[0] = _buffer;
		_bufferList.mNumberBuffers = 1;
		
		_mutableAudioBufferList = &_bufferList;
		_audioBufferList = &_bufferList;

	}

	return self;
}

- (IOSFloatArray *)getBuffer {
	return _channelBuffer;
}



@end
