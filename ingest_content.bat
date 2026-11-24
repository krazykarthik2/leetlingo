@echo off
REM ============================================================
REM LeetLingo Agentic CLI - Ingest AI Content Batch
REM ============================================================
echo [LeetLingo] Running AI Content Ingestion Batch...
python scripts\content_agent.py
if %ERRORLEVEL% EQU 0 (
    echo [LeetLingo] Batch Content Ingestion Completed Successfully.
) else (
    echo [LeetLingo] Failed to ingest content.
)
pause
