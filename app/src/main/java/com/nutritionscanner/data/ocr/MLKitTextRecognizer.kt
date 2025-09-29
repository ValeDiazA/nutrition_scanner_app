package com.nutritionscanner.data.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class MLKitTextRecognizer @Inject constructor() {
    
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    
    suspend fun recognizeText(bitmap: Bitmap): Result<String> = suspendCancellableCoroutine { continuation ->
        val image = InputImage.fromBitmap(bitmap, 0)
        
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val recognizedText = visionText.text
                continuation.resume(Result.success(recognizedText))
            }
            .addOnFailureListener { exception ->
                continuation.resume(Result.failure(exception))
            }
            
        continuation.invokeOnCancellation {
            // Cleanup if needed
        }
    }
    
    suspend fun recognizeTextWithBlocks(bitmap: Bitmap): Result<List<TextBlock>> = suspendCancellableCoroutine { continuation ->
        val image = InputImage.fromBitmap(bitmap, 0)
        
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val textBlocks = visionText.textBlocks.map { block ->
                    TextBlock(
                        text = block.text,
                        boundingBox = block.boundingBox,
                        confidence = 0f, // ML Kit no proporciona confidence por bloque
                        lines = block.lines.map { line ->
                            TextLine(
                                text = line.text,
                                boundingBox = line.boundingBox,
                                confidence = 0f // ML Kit no proporciona confidence por línea
                            )
                        }
                    )
                }
                continuation.resume(Result.success(textBlocks))
            }
            .addOnFailureListener { exception ->
                continuation.resume(Result.failure(exception))
            }
            
        continuation.invokeOnCancellation {
            // Cleanup if needed  
        }
    }
}

data class TextBlock(
    val text: String,
    val boundingBox: android.graphics.Rect?,
    val confidence: Float,
    val lines: List<TextLine>
)

data class TextLine(
    val text: String,
    val boundingBox: android.graphics.Rect?,
    val confidence: Float
)