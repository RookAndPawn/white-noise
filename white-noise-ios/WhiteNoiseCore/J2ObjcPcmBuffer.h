//
//  J2ObjcPcmBuffer.h
//  White Noise IOS
//
//  Created by Kevin Guthrie on 12/31/16.
//  Copyright © 2016 RookAndPawn. All rights reserved.
//

#import <JRE/JRE.h>
#import <AVFoundation/AVFoundation.h>
#import "HasBuffer.h"

@interface J2ObjcPcmBuffer : AVAudioPCMBuffer <RPAVHasBuffer> {
	@protected
	AudioBuffer _buffer;
	AudioBufferList _bufferList;
}


/*!	@method initWithPCMFormat:frameCapacity:
	@abstract Initialize a buffer that is to contain PCM audio samples.
	@param format
 The format of the PCM audio to be contained in the buffer.
	@param frameCapacity
 The capacity of the buffer in PCM sample frames.
	@discussion
 An exception is raised if the format is not PCM.
 */
- (nonnull instancetype)initWithPCMFormat:(nonnull AVAudioFormat *) format
		frameCapacity:(AVAudioFrameCount)frameCapacity;


@property (readonly,nonatomic) IOSFloatArray * __nonnull channelBuffer;

/*!	@property audioBufferList
	@abstract The buffer's underlying AudioBufferList.
	@discussion
 For compatibility with lower-level CoreAudio and AudioToolbox API's, this method accesses
 the buffer implementation's internal AudioBufferList. The buffer list structure must
 not be modified, though you may modify buffer contents.

 The mDataByteSize fields of this AudioBufferList express the buffer's current frameLength.
 */
@property (nonatomic, readonly) const AudioBufferList * __nonnull audioBufferList;

/*!	@property mutableAudioBufferList
	@abstract A mutable version of the buffer's underlying AudioBufferList.
	@discussion
 Some lower-level CoreAudio and AudioToolbox API's require a mutable AudioBufferList,
 for example, AudioConverterConvertComplexBuffer.

 The mDataByteSize fields of this AudioBufferList express the buffer's current frameCapacity.
 If they are altered, you should modify the buffer's frameLength to match.
 */
@property (nonatomic, readonly) AudioBufferList * __nonnull mutableAudioBufferList;



@end
