@set path=D:\Program Files\MySQL\MySQL Server 5.1\bin\;%path%
@for %%i in (%CD%\output_*.txt) do @(
echo %%i
mysql -u root "" --default-character-set=utf8 < %%i
)
@pause
