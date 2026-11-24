package com.leetlingo.app.data.model

data class Option(
    val id: String,
    val title: String,
    val description: String,
    val isCorrect: Boolean,
    val explanation: String
)

data class ReelProblem(
    val id: String,
    val title: String,
    val category: String, // "algorithmic" | "brain_twister"
    val difficulty: String,
    val topic: String,
    val statement: String,
    val inputSample: String,
    val outputSample: String,
    val patternTag: String,
    val options: List<Option>,
    val pseudoKeywords: List<String>,
    val pseudoHint: String,
    val pythonSolution: String,
    val leetCodeSlug: String
)

data class Concept(
    val id: String,
    val name: String,
    val topic: String,
    val tagline: String,
    val coreIdea: String,
    val whenToUse: List<String>,
    val prerequisites: List<String>,
    val practiceReelIds: List<String>
)

data class WeaknessTopic(
    val name: String,
    val masteryScore: Int,
    val attempted: Int,
    val won: Int,
    val isBiggestBleed: Boolean
)

data class WeaknessProfile(
    val topics: List<WeaknessTopic>,
    val summary: String
)

enum class MemoryState {
    FRESH, FADING, COLD, CONQUERED
}

data class VaultItem(
    val problem: ReelProblem,
    val userPseudoCode: String,
    val wonTimestampMillis: Long,
    val memoryState: MemoryState
)
