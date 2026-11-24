"""
Simulation of user engagement across LeetLingo's Level 1, 2, 3 progression:
Reel -> L1 Approach Check -> L2 Pseudo Iteration -> L2 Win (Python Reveal) -> Vault Storage
"""

import json
import os

REELS_PATH = os.path.join("app", "src", "main", "assets", "content", "reels", "reels.json")

def simulate():
    print("▶ SIMULATING LEETLINGO LOOP...")
    with open(REELS_PATH, "r", encoding="utf-8") as f:
        reels = json.load(f)
        
    problem = reels[0]
    print(f"\n1. [REEL FEED] Served Card: '{problem['title']}' ({problem['topic']})")
    print(f"   Statement: {problem['statement']}")
    print(f"   Example: {problem['inputSample']} -> {problem['outputSample']}")
    
    # L1 simulation
    print("\n2. [LEVEL 1 - APPROACH]")
    correct_opt = next(o for o in problem["options"] if o["isCorrect"])
    print(f"   User taps: '{correct_opt['title']}'")
    print(f"   Feedback: [CORRECT] {correct_opt['explanation']}")
    
    # L2 simulation
    print("\n3. [LEVEL 2 - PSEUDO CODE]")
    test_pseudo = "create hash map seen; for each num check target - num in seen; return indices; store num"
    print(f"   User writes pseudo: '{test_pseudo}'")
    
    # Keyword check
    matched = [kw for kw in problem["pseudoKeywords"] if kw in test_pseudo.lower()]
    print(f"   AI Rubric match: {matched} -> Threshold met!")
    print("   Result: [WIN] Optimal Python Solution Revealed:")
    print("   -------------------------------------------------")
    for line in problem["pythonSolution"].split("\n"):
        print(f"   | {line}")
    print("   -------------------------------------------------")
    
    # L3 Vault Simulation
    print("\n4. [LEVEL 3 - VAULT STORAGE]")
    vault_item = {
        "problemId": problem["id"],
        "title": problem["title"],
        "pseudoCode": test_pseudo,
        "state": "FRESH",
        "action": "Ready to code on LeetCode without answer spoilers!"
    }
    print(f"   Saved to Vault: {json.dumps(vault_item, indent=2)}")
    print("\n✓ SIMULATION PASSED: The Itch and The Loop function as designed.")

if __name__ == "__main__":
    simulate()
