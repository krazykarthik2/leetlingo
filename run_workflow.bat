@echo off
REM ============================================================
REM LeetLingo Agentic CLI - Run Full Test Demonstration
REM ============================================================
echo ============================================================
echo   LEETLINGO AGENTIC SYSTEM - REPETITIVE WORKFLOW RUNNER
echo ============================================================
echo 1. Validating Decoupled Content Files...
call python scripts\content_agent.py --validate
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Content validation failed!
    exit /b %ERRORLEVEL%
)

echo.
echo 2. Running Automated Logic Test Simulator...
call python scripts\simulate_flow.py
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Flow simulation failed!
    exit /b %ERRORLEVEL%
)

echo.
echo [DONE] All automated agent workflows succeeded!
pause
