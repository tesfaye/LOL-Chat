@echo off 
for %%a in (%folder%\*) do (
  echo ren "%%~fa" "champion%%~nxa"
)