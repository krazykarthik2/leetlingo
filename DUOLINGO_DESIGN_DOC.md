# Duolingo Design System — Design Style Reference

```yaml
# How this file was made. Sources are first-party; nothing here is
# read from a rendered page. Full breakdown at source_page below.
method: restated-from-official-docs
confidence: medium
verified_at: unknown
source_page: https://www.designsystems.one/design-systems/duolingo-design
sources:
  - https://design.duolingo.com/
  - https://design.duolingo.com/identity/color
```

> **Unofficial, community-authored reference.** Compiled by [designsystems.one](https://www.designsystems.one). Not affiliated with, endorsed by, or sponsored by Duolingo. "Duolingo Design System" and related marks belong to their respective owners. This document describes publicly observable style attributes in original words — it contains no copied documentation, logos, icon artwork, or original token files. Always defer to the official source for canonical, current values.

**System:** Duolingo Design System
**Owner:** Duolingo
**Official documentation:** https://design.duolingo.com/
**Visual character:** Gamified bird-mascot joy: feather green everything, chunky 3D-shadow buttons, rounded type, and animal-named colors that keep streaks alive.

---

## Summary

Design system for Duolingo's language learning platform with playful, engaging interfaces.

_Editorial confidence: medium. Most values are publicly documented; a few are best-effort readings of the live product. Field-level evidence: 0/73 claims source-linked, 0/73 dated. Confidence is not verification; validate canonical values before implementation._

[Inspect every field and its evidence](https://www.designsystems.one/api/style-spec?system=duolingo-design)

## Color tokens

| Color | Value | CSS variable | Role |
| --- | --- | --- | --- |
| Feather Green | `#58cc02` | `--duo-feather-green` | Brand and primary CTAs |
| Mask Green | `#89e219` | `--duo-mask-green` | Owl highlight green |
| Eel Text | `#4b4b4b` | `--duo-eel` | Primary text |
| Wolf Secondary | `#777777` | `--duo-wolf` | Secondary text |
| Snow | `#ffffff` | `--duo-snow` | Surfaces |
| Polar | `#f7f7f7` | `--duo-polar` | Subtle background |
| Swan Border | `#e5e5e5` | `--duo-swan` | Borders |
| Macaw Blue | `#1cb0f6` | `--duo-macaw` | Links and info |
| Cardinal Red | `#ff4b4b` | `--duo-cardinal` | Errors and hearts |
| Bee Yellow | `#ffc800` | `--duo-bee` | Streaks and rewards |

## Typography

- **Body:** Feather / DIN Next Rounded — weights 400, 700 — `'feather', 'din-round', 'Varela Round', sans-serif`

## Type scale

| Step | Size | Line height |
| --- | --- | --- |
| h1 | `32px` | `40px` |
| h2 | `24px` | `32px` |
| body | `17px` | `24px` |
| small | `14px` | `20px` |

## Spacing

Base unit: `8px`

Scale: `8px` · `16px` · `24px` · `32px` · `48px`

## Corner radius

| Name | Value |
| --- | --- |
| default | `12px` |
| large | `16px` |
| pill | `9999px` |

## Elevation / shadows

| Name | Value |
| --- | --- |
| button-3d | `0 4px 0 rgba(0,0,0,0.2)` |

## Motion

| Name | Value |
| --- | --- |
| bounce | `300ms cubic-bezier(0.68, -0.55, 0.265, 1.55)` |

## Quick start (CSS)

```css
/* Duolingo Design System — illustrative tokens, restated as CSS custom properties.
   Unofficial reference by designsystems.one; values are indicative, not
   canonical. Native dp/sp values are omitted, never silently converted.
   Official reference: https://design.duolingo.com/
   Field evidence: https://www.designsystems.one/api/style-spec?system=duolingo-design */

:root {
  /* Color */
  --duo-feather-green: #58cc02; /* Brand and primary CTAs */
  --duo-mask-green: #89e219; /* Owl highlight green */
  --duo-eel: #4b4b4b; /* Primary text */
  --duo-wolf: #777777; /* Secondary text */
  --duo-snow: #ffffff; /* Surfaces */
  --duo-polar: #f7f7f7; /* Subtle background */
  --duo-swan: #e5e5e5; /* Borders */
  --duo-macaw: #1cb0f6; /* Links and info */
  --duo-cardinal: #ff4b4b; /* Errors and hearts */
  --duo-bee: #ffc800; /* Streaks and rewards */

  /* Typography */
  --font-body: 'feather', 'din-round', 'Varela Round', sans-serif;

  /* Type scale */
  --text-h1: 32px; /* line-height 40px */
  --text-h2: 24px; /* line-height 32px */
  --text-body: 17px; /* line-height 24px */
  --text-small: 14px; /* line-height 20px */

  /* Spacing */
  --space-1: 8px;
  --space-2: 16px;
  --space-3: 24px;
  --space-4: 32px;
  --space-5: 48px;

  /* Radius */
  --radius-default: 12px;
  --radius-large: 16px;
  --radius-pill: 9999px;

  /* Elevation */
  --shadow-button-3d: 0 4px 0 rgba(0,0,0,0.2);

  /* Motion */
  --motion-bounce: 300ms cubic-bezier(0.68, -0.55, 0.265, 1.55);
}
```

## Engineering profile

- **Ships for:** React
- **Source model:** Closed

## Apply this style with an AI tool

Paste this into your AI coding assistant as a direction, then iterate:

> Design in the spirit of Duolingo Design System by Duolingo — Design system for Duolingo's language learning platform with playful, engaging interfaces. Character: Gamified bird-mascot joy: feather green everything, chunky 3D-shadow buttons, rounded type, and animal-named colors that keep streaks alive. Use a primary colour near #58cc02, the typeface "Feather / DIN Next Rounded" (or a close equivalent), corner radius around 12px, a spacing rhythm on a 8px base. Match the intent, not the pixels; adapt it to my product rather than cloning it.

## Official reference

- [Duolingo Design System documentation](https://design.duolingo.com/)

---

_Generated by [designsystems.one](https://www.designsystems.one/design-systems/duolingo-design) — a catalogue of public design systems. Style facts are reported for reference and education; adapt them to your own product. Report issues or takedown requests via the site._
