"""
Agentic Content Generator for LeetLingo
Allows batch-ingesting algorithms, brain twisters, and concepts on-the-fly.
Decoupled completely from application code.
"""

import json
import os
import sys

REELS_PATH = os.path.join("app", "src", "main", "assets", "content", "reels", "reels.json")
CONCEPTS_PATH = os.path.join("app", "src", "main", "assets", "content", "concepts", "concepts.json")

def add_reel(problem_dict):
    os.makedirs(os.path.dirname(REELS_PATH), exist_ok=True)
    reels = []
    if os.path.exists(REELS_PATH):
        with open(REELS_PATH, "r", encoding="utf-8") as f:
            try:
                reels = json.load(f)
            except Exception:
                reels = []
    
    # Check duplicate
    if any(r.get("id") == problem_dict.get("id") for r in reels):
        print(f"[SKIP] Problem with ID {problem_dict.get('id')} already exists.")
        return
        
    reels.append(problem_dict)
    with open(REELS_PATH, "w", encoding="utf-8") as f:
        json.dump(reels, f, indent=2)
    print(f"[SUCCESS] Appended '{problem_dict.get('title')}' into {REELS_PATH}")

def validate_content():
    print("--- VALIDATING LEETLINGO CONTENT INTEGRITY ---")
    if not os.path.exists(REELS_PATH):
        print(f"[ERROR] Missing {REELS_PATH}")
        return False
        
    with open(REELS_PATH, "r", encoding="utf-8") as f:
        reels = json.load(f)
        print(f"✓ Found {len(reels)} reel problems.")
        for r in reels:
            assert "id" in r and "title" in r and "statement" in r
            assert len(r.get("options", [])) == 4, f"Reel {r['id']} must have exactly 4 options!"
            correct_opts = [o for o in r["options"] if o.get("isCorrect")]
            assert len(correct_opts) == 1, f"Reel {r['id']} must have exactly 1 correct option!"
            assert "pythonSolution" in r
            print(f"  - [{r['category'].upper()}] {r['title']} ({r['topic']}) -> OK")
            
    if os.path.exists(CONCEPTS_PATH):
        with open(CONCEPTS_PATH, "r", encoding="utf-8") as f:
            concepts = json.load(f)
            print(f"✓ Found {len(concepts)} frontier concepts.")
            for c in concepts:
                print(f"  - Concept: {c['name']} -> OK")
                
    print("--- ALL CONTENT VALIDATED SUCCESSFULLY ---")
    return True

if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "--validate":
        validate_content()
    else:
        # Example batch generation
        sample_twister = {
            "id": f"reel_{len(json.load(open(REELS_PATH))) + 1:03d}",
            "title": "Missing Number in Range",
            "category": "brain_twister",
            "difficulty": "Easy",
            "topic": "Bit Manipulation & Math",
            "statement": "Given an array nums containing n distinct numbers in the range [0, n], return the only number missing from the range.",
            "inputSample": "nums = [3, 0, 1]",
            "outputSample": "2",
            "patternTag": "Gauss Sum or XOR",
            "options": [
                {
                    "id": "opt_1",
                    "title": "Gauss Formula n*(n+1)//2",
                    "description": "Subtract sum(nums) from expected arithmetic progression sum",
                    "isCorrect": True,
                    "explanation": "Runs in O(n) time and O(1) space with minimal lines of logic!"
                },
                {
                    "id": "opt_2",
                    "title": "Sorting",
                    "description": "Sort and check where indices mismatch",
                    "isCorrect": False,
                    "explanation": "Sorting requires O(n log n) time."
                },
                {
                    "id": "opt_3",
                    "title": "Nested Search",
                    "description": "Check if each i from 0..n is in nums",
                    "isCorrect": False,
                    "explanation": "Takes O(n²) time."
                },
                {
                    "id": "opt_4",
                    "title": "Frequency Array",
                    "description": "Allocate array of size n+1",
                    "isCorrect": False,
                    "explanation": "Takes O(n) auxiliary memory. O(1) space is achievable."
                }
            ],
            "pseudoKeywords": ["sum", "expected", "len", "n", "minus"],
            "pseudoHint": "Calculate expected sum from 0 to n using n*(n+1)//2, then subtract the actual sum of array elements.",
            "pythonSolution": "def missingNumber(nums: list[int]) -> int:\n    n = len(nums)\n    expected = n * (n + 1) // 2\n    return expected - sum(nums)\n# Time: O(n) | Space: O(1)",
            "leetCodeSlug": "missing-number"
        }
        add_reel(sample_twister)
        validate_content()
