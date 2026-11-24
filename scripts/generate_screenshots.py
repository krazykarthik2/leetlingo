"""
Generates high-fidelity mobile screenshots of LeetLingo's screens:
1. Reel Mode (Electric Blue)
2. L1: Approach Picker (Modal)
3. L2: Pseudo Verification & Python Win
4. Reflect Mode (Crimson Red)
5. Ascend Mode (Emerald Green)
6. Vault (Amber Gold)
7. Account (Aesthetic Noise + Pitch Black)
"""

from PIL import Image, ImageDraw, ImageFont
import os

SCREEN_W = 420
SCREEN_H = 860
SCREENSHOTS_DIR = os.path.join("docs", "screenshots")
os.makedirs(SCREENSHOTS_DIR, exist_ok=True)

def get_font(size, bold=False):
    try:
        # Standard Windows fonts
        font_name = "segoeuib.ttf" if bold else "segoeui.ttf"
        return ImageFont.truetype(font_name, size)
    except Exception:
        try:
            font_name = "arialbd.ttf" if bold else "arial.ttf"
            return ImageFont.truetype(font_name, size)
        except Exception:
            return ImageFont.load_default()

def draw_phone_frame(draw, bg_color):
    # Outer background
    draw.rectangle([0, 0, SCREEN_W, SCREEN_H], fill=bg_color)
    # Status bar
    font_sm = get_font(13)
    draw.text((32, 14), "9:41", fill=(200, 200, 200), font=font_sm)
    draw.text((SCREEN_W - 70, 14), "5G  100%", fill=(200, 200, 200), font=font_sm)

def draw_bottom_bar(draw, active_idx=0):
    y_start = SCREEN_H - 74
    draw.rectangle([0, y_start, SCREEN_W, SCREEN_H], fill=(10, 15, 25))
    draw.line([0, y_start, SCREEN_W, y_start], fill=(25, 35, 50), width=1)
    
    tabs = [
        ("Reel", (0, 102, 255)),
        ("Reflect", (239, 68, 68)),
        ("Ascend", (16, 185, 129)),
        ("Vault", (245, 158, 11)),
        ("Account", (255, 255, 255))
    ]
    
    col_w = SCREEN_W / 5
    font = get_font(11, bold=True)
    font_icon = get_font(16)
    icons = ["* ", "@ ", "^ ", "# ", "$ "]
    
    for i, (name, col) in enumerate(tabs):
        cx = i * col_w + col_w / 2
        is_active = (i == active_idx)
        text_color = col if is_active else (100, 116, 139)
        # Dot indicator if active
        if is_active:
            draw.ellipse([cx - 3, y_start + 8, cx + 3, y_start + 14], fill=col)
        draw.text((cx - 16, y_start + 24), name, fill=text_color, font=font)

def draw_chunky_button(draw, x, y, w, h, text, color, text_color=(255, 255, 255)):
    # 3D shadow bottom
    shadow_col = (max(0, color[0]-50), max(0, color[1]-50), max(0, color[2]-50))
    draw.rounded_rectangle([x, y + 4, x + w, y + h + 4], radius=16, fill=shadow_col)
    # Top face
    draw.rounded_rectangle([x, y, x + w, y + h], radius=16, fill=color)
    font = get_font(15, bold=True)
    bbox = draw.textbbox((0, 0), text, font=font)
    tw = bbox[2] - bbox[0]
    draw.text((x + (w - tw) / 2, y + (h - 18) / 2), text, fill=text_color, font=font)

# 1. REEL SCREEN
def make_reel():
    img = Image.new("RGB", (SCREEN_W, SCREEN_H), (7, 11, 20))
    d = ImageDraw.Draw(img)
    draw_phone_frame(d, (7, 11, 20))
    
    # Header
    d.text((24, 48), "REEL", fill=(56, 189, 248), font=get_font(22, bold=True))
    d.text((SCREEN_W - 130, 52), "7d Streak", fill=(255, 200, 0), font=get_font(13, bold=True))
    
    # Card
    card_box = [20, 90, SCREEN_W - 20, SCREEN_H - 100]
    d.rounded_rectangle(card_box, radius=24, fill=(15, 23, 42), outline=(0, 102, 255), width=2)
    
    # Card Header
    d.text((42, 116), "TWO SUM", fill=(255, 255, 255), font=get_font(22, bold=True))
    d.rounded_rectangle([SCREEN_W - 140, 114, SCREEN_W - 42, 142], radius=8, fill=(0, 102, 255, 50), outline=(0, 102, 255), width=1)
    d.text((SCREEN_W - 128, 120), "Hash Map", fill=(56, 189, 248), font=get_font(12, bold=True))
    
    # Statement
    stmt = ("Given an array of integers nums and an\n"
            "integer target, return indices of the two\n"
            "numbers such that they add up to target.\n\n"
            "Assume exactly one solution exists.")
    d.text((42, 170), stmt, fill=(203, 213, 225), font=get_font(15))
    
    # Example Box
    d.rounded_rectangle([42, 290, SCREEN_W - 42, 380], radius=14, fill=(7, 11, 20))
    d.text((56, 302), "Example 1:", fill=(100, 116, 139), font=get_font(12, bold=True))
    d.text((56, 324), "nums = [2, 7, 11, 15], target = 9", fill=(255, 255, 255), font=get_font(13))
    d.text((56, 348), "-> Output: [0, 1]", fill=(56, 189, 248), font=get_font(13, bold=True))
    
    # Gesture Guide
    d.text((SCREEN_W/2 - 120, SCREEN_H - 220), "Swipe Up to Pass  |  Swipe Right if Hard", fill=(100, 116, 139), font=get_font(12))
    
    # Single Chunky Button
    draw_chunky_button(d, 42, SCREEN_H - 180, SCREEN_W - 84, 56, "CRACK THE APPROACH", (0, 102, 255))
    
    draw_bottom_bar(d, 0)
    img.save(os.path.join(SCREENSHOTS_DIR, "01_reel_screen.png"))

# 2. REFLECT SCREEN
def make_reflect():
    img = Image.new("RGB", (SCREEN_W, SCREEN_H), (15, 8, 8))
    d = ImageDraw.Draw(img)
    draw_phone_frame(d, (15, 8, 8))
    
    d.text((24, 48), "REFLECT", fill=(239, 68, 68), font=get_font(22, bold=True))
    d.text((24, 76), "Weakness Radar Diagnostic", fill=(148, 163, 184), font=get_font(13))
    
    # Radar Card
    d.rounded_rectangle([20, 110, SCREEN_W - 20, 390], radius=20, fill=(31, 13, 13), outline=(239, 68, 68), width=1)
    
    topics = [
        ("Bit Manipulation *Bleed*", 24, True),
        ("Dynamic Programming", 38, False),
        ("Graph Traversal", 51, False),
        ("Stack & Queues", 68, False),
        ("Trees & Binary Search", 72, False),
        ("Hash Maps & Sets", 95, False)
    ]
    
    y = 130
    for name, score, is_bleed in topics:
        col = (239, 68, 68) if is_bleed else (255, 255, 255)
        d.text((36, y), name, fill=col, font=get_font(13, bold=is_bleed))
        d.text((SCREEN_W - 80, y), f"{score}%", fill=(148, 163, 184), font=get_font(12))
        
        # Bar track
        d.rounded_rectangle([36, y + 20, SCREEN_W - 36, y + 26], radius=3, fill=(30, 41, 59))
        bar_w = int((SCREEN_W - 72) * (score / 100))
        d.rounded_rectangle([36, y + 20, 36 + bar_w, y + 26], radius=3, fill=(239, 68, 68) if is_bleed else (100, 116, 139))
        y += 40
        
    # Critical gap box
    d.rounded_rectangle([20, 410, SCREEN_W - 20, 530], radius=16, fill=(43, 10, 10), outline=(239, 68, 68), width=1)
    d.text((36, 424), "CRITICAL GAP DETECTED", fill=(239, 68, 68), font=get_font(12, bold=True))
    d.text((36, 444), "Bit Manipulation (3/12 won)", fill=(255, 255, 255), font=get_font(16, bold=True))
    d.text((36, 470), "75% bailed as 'Too Hard'. Your biggest\ngap is XOR cancellation and bitmasking.", fill=(226, 232, 240), font=get_font(13))
    
    draw_chunky_button(d, 24, SCREEN_H - 160, SCREEN_W - 48, 56, "ATTACK BIGGEST GAP", (239, 68, 68))
    draw_bottom_bar(d, 1)
    img.save(os.path.join(SCREENSHOTS_DIR, "02_reflect_screen.png"))

# 3. ASCEND SCREEN
def make_ascend():
    img = Image.new("RGB", (SCREEN_W, SCREEN_H), (6, 17, 11))
    d = ImageDraw.Draw(img)
    draw_phone_frame(d, (6, 17, 11))
    
    d.text((24, 48), "ASCEND", fill=(16, 185, 129), font=get_font(22, bold=True))
    d.text((24, 76), "ML-Suggested Frontier Concept", fill=(148, 163, 184), font=get_font(13))
    
    # Concept Card
    d.rounded_rectangle([20, 110, SCREEN_W - 20, 560], radius=24, fill=(12, 31, 21), outline=(16, 185, 129), width=2)
    
    d.rounded_rectangle([38, 130, 160, 158], radius=8, fill=(16, 185, 129, 60))
    d.text((46, 136), "Stack & Greedy", fill=(110, 231, 183), font=get_font(12, bold=True))
    
    d.text((38, 176), "Monotonic Stack", fill=(250, 250, 250), font=get_font(22, bold=True))
    d.text((38, 206), "Next Greater Element in O(n) Runtime", fill=(167, 243, 208), font=get_font(13))
    
    d.text((38, 248), "Core Pattern:", fill=(110, 231, 183), font=get_font(13, bold=True))
    pat = ("A stack maintained in strictly increasing\n"
           "or decreasing order. Popping violates\n"
           "until balance is restored.")
    d.text((38, 270), pat, fill=(226, 232, 240), font=get_font(14))
    
    d.text((38, 350), "When to Apply in Interviews:", fill=(110, 231, 183), font=get_font(13, bold=True))
    bullets = [
        "- Next greater or smaller element",
        "- Largest rectangle in histogram",
        "- Daily temperatures & stock span"
    ]
    by = 376
    for b in bullets:
        d.text((38, by), b, fill=(203, 213, 225), font=get_font(13))
        by += 26
        
    draw_chunky_button(d, 24, SCREEN_H - 160, SCREEN_W - 48, 56, "ABSORB PATTERN & DRILL", (16, 185, 129))
    draw_bottom_bar(d, 2)
    img.save(os.path.join(SCREENSHOTS_DIR, "03_ascend_screen.png"))

# 4. VAULT SCREEN
def make_vault():
    img = Image.new("RGB", (SCREEN_W, SCREEN_H), (13, 13, 13))
    d = ImageDraw.Draw(img)
    draw_phone_frame(d, (13, 13, 13))
    
    d.text((24, 48), "VAULT", fill=(245, 158, 11), font=get_font(22, bold=True))
    d.text((SCREEN_W - 100, 52), "47 Won", fill=(148, 163, 184), font=get_font(14))
    
    # Filter chips
    chips = ["ALL", "FADING", "COLD", "DONE"]
    cx = 24
    for i, c in enumerate(chips):
        is_sel = (i == 0)
        bg = (245, 158, 11) if is_sel else (31, 31, 31)
        fg = (0, 0, 0) if is_sel else (148, 163, 184)
        d.rounded_rectangle([cx, 86, cx + 70, 116], radius=15, fill=bg)
        d.text((cx + 18, 94), c, fill=fg, font=get_font(12, bold=True))
        cx += 80
        
    # Item Card 1
    d.rounded_rectangle([20, 136, SCREEN_W - 20, 276], radius=16, fill=(26, 26, 26))
    d.text((36, 150), "Two Sum", fill=(255, 255, 255), font=get_font(16, bold=True))
    d.text((SCREEN_W - 100, 150), "Fresh", fill=(16, 185, 129), font=get_font(12, bold=True))
    d.text((36, 176), "Your Pseudo Logic:", fill=(148, 163, 184), font=get_font(12))
    d.text((36, 194), "init map, check target-num in map, return indices", fill=(226, 232, 240), font=get_font(13))
    draw_chunky_button(d, 36, 222, SCREEN_W - 72, 40, "CODE ON LEETCODE", (245, 158, 11), text_color=(0, 0, 0))
    
    # Item Card 2
    d.rounded_rectangle([20, 296, SCREEN_W - 20, 436], radius=16, fill=(26, 26, 26))
    d.text((36, 310), "LRU Cache", fill=(255, 255, 255), font=get_font(16, bold=True))
    d.text((SCREEN_W - 100, 310), "Fading", fill=(255, 200, 0), font=get_font(12, bold=True))
    d.text((36, 336), "Your Pseudo Logic:", fill=(148, 163, 184), font=get_font(12))
    d.text((36, 354), "hashmap + doubly linked list with dummy head/tail", fill=(226, 232, 240), font=get_font(13))
    draw_chunky_button(d, 36, 382, SCREEN_W - 72, 40, "RE-ATTEMPT L2", (40, 40, 40), text_color=(255, 255, 255))
    
    draw_bottom_bar(d, 3)
    img.save(os.path.join(SCREENSHOTS_DIR, "04_vault_screen.png"))

# 5. ACCOUNT SCREEN
def make_account():
    img = Image.new("RGB", (SCREEN_W, SCREEN_H), (0, 0, 0))
    d = ImageDraw.Draw(img)
    draw_phone_frame(d, (0, 0, 0))
    
    d.text((24, 48), "ACCOUNT", fill=(255, 255, 255), font=get_font(22, bold=True))
    d.text((24, 76), "Aesthetic Noise + Pitch Black OLED", fill=(115, 115, 115), font=get_font(13))
    
    # Profile Card
    d.rounded_rectangle([20, 110, SCREEN_W - 20, 210], radius=20, fill=(10, 10, 10), outline=(38, 38, 38), width=1)
    d.text((40, 126), "KARTHIK", fill=(255, 255, 255), font=get_font(20, bold=True))
    d.text((40, 154), "karthik@domain.dev", fill=(115, 115, 115), font=get_font(14))
    d.text((40, 178), "12d Streak   |   47 Conquered in Vault", fill=(255, 200, 0), font=get_font(13, bold=True))
    
    # Lifetime Snapshot
    d.text((24, 236), "LIFETIME SNAPSHOT", fill=(115, 115, 115), font=get_font(12, bold=True))
    d.rounded_rectangle([20, 258, SCREEN_W - 20, 430], radius=16, fill=(10, 10, 10), outline=(38, 38, 38), width=1)
    
    stats = [
        ("Cards Cracked (L1)", "218"),
        ("Pseudo Wins (L2)", "47"),
        ("LeetCode Shipped", "11"),
        ("Patterns Absorbed", "8")
    ]
    sy = 276
    for k, v in stats:
        d.text((38, sy), k, fill=(255, 255, 255), font=get_font(14))
        d.text((SCREEN_W - 70, sy), v, fill=(115, 115, 115), font=get_font(14, bold=True))
        sy += 36
        
    draw_chunky_button(d, 24, SCREEN_H - 160, SCREEN_W - 48, 56, "SYSTEM PREFERENCES", (255, 255, 255), text_color=(0, 0, 0))
    draw_bottom_bar(d, 4)
    img.save(os.path.join(SCREENSHOTS_DIR, "05_account_screen.png"))

if __name__ == "__main__":
    make_reel()
    make_reflect()
    make_ascend()
    make_vault()
    make_account()
    print("✓ All screenshots generated in docs/screenshots/")
