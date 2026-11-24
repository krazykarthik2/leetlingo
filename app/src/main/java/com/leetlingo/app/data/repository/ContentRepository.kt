package com.leetlingo.app.data.repository

import android.content.Context
import com.leetlingo.app.data.model.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * ContentRepository loads decoupled JSON content from assets.
 * Content can be refreshed, added or ingested dynamically via AI agents.
 */
class ContentRepository(private val context: Context) {

    fun loadReels(): List<ReelProblem> {
        val jsonString = readAsset("content/reels/reels.json") ?: return emptyList()
        val jsonArray = JSONArray(jsonString)
        val list = mutableListOf<ReelProblem>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val optionsArray = obj.getJSONArray("options")
            val options = mutableListOf<Option>()
            for (j in 0 until optionsArray.length()) {
                val opt = optionsArray.getJSONObject(j)
                options.add(
                    Option(
                        id = opt.getString("id"),
                        title = opt.getString("title"),
                        description = opt.getString("description"),
                        isCorrect = opt.getBoolean("isCorrect"),
                        explanation = opt.getString("explanation")
                    )
                )
            }

            val kwArray = obj.getJSONArray("pseudoKeywords")
            val keywords = mutableListOf<String>()
            for (k in 0 until kwArray.length()) {
                keywords.add(kwArray.getString(k))
            }

            list.add(
                ReelProblem(
                    id = obj.getString("id"),
                    title = obj.getString("title"),
                    category = obj.getString("category"),
                    difficulty = obj.getString("difficulty"),
                    topic = obj.getString("topic"),
                    statement = obj.getString("statement"),
                    inputSample = obj.getString("inputSample"),
                    outputSample = obj.getString("outputSample"),
                    patternTag = obj.getString("patternTag"),
                    options = options,
                    pseudoKeywords = keywords,
                    pseudoHint = obj.getString("pseudoHint"),
                    pythonSolution = obj.getString("pythonSolution"),
                    leetCodeSlug = obj.getString("leetCodeSlug")
                )
            )
        }
        return list
    }

    fun loadConcepts(): List<Concept> {
        val jsonString = readAsset("content/concepts/concepts.json") ?: return emptyList()
        val array = JSONArray(jsonString)
        val list = mutableListOf<Concept>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val whenArray = obj.getJSONArray("whenToUse")
            val whenList = (0 until whenArray.length()).map { whenArray.getString(it) }
            val prereqArray = obj.getJSONArray("prerequisites")
            val prereqList = (0 until prereqArray.length()).map { prereqArray.getString(it) }
            val pReelArray = obj.getJSONArray("practiceReelIds")
            val pReelList = (0 until pReelArray.length()).map { pReelArray.getString(it) }

            list.add(
                Concept(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    topic = obj.getString("topic"),
                    tagline = obj.getString("tagline"),
                    coreIdea = obj.getString("coreIdea"),
                    whenToUse = whenList,
                    prerequisites = prereqList,
                    practiceReelIds = pReelList
                )
            )
        }
        return list
    }

    fun loadWeaknessProfile(): WeaknessProfile {
        val jsonString = readAsset("content/weaknesses/weakness_profile.json") ?: return WeaknessProfile(emptyList(), "")
        val obj = JSONObject(jsonString)
        val topicsArray = obj.getJSONArray("topics")
        val topics = mutableListOf<WeaknessTopic>()
        for (i in 0 until topicsArray.length()) {
            val t = topicsArray.getJSONObject(i)
            topics.add(
                WeaknessTopic(
                    name = t.getString("name"),
                    masteryScore = t.getInt("masteryScore"),
                    attempted = t.getInt("attempted"),
                    won = t.getInt("won"),
                    isBiggestBleed = t.getBoolean("isBiggestBleed")
                )
            )
        }
        return WeaknessProfile(topics, obj.getString("summary"))
    }

    private fun readAsset(path: String): String? {
        return try {
            context.assets.open(path).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            null
        }
    }
}
