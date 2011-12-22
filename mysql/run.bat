@for %%i in (%CD%\output_*.txt) do @(
echo %%i
mysql -u root "" < %%i
)
@pause
