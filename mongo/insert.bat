@for %%i in (%CD%\output_*.js) do @(
echo %%i
mongo.exe %%i
)
@pause
