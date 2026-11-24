@echo off
REM ============================================================
REM LeetLingo Agentic CLI - Validate Content Integrity
REM ============================================================
echo [LeetLingo] Validating JSON schema and problem options...
python scripts\content_agent.py --validate
pause
