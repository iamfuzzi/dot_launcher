chcp 65001
@echo off
setlocal enabledelayedexpansion

rem Устанавливаем переменные для путей
set PROJECT_DIR=%~dp0
set SRC_DIR=%PROJECT_DIR%source\src
set LIB_DIR=%SRC_DIR%\me\fuzzi\dot\launcher\libraries
set MAIN_CLASS=me.fuzzi.dot.launcher.classes.Main

rem Компилируем все Java файлы
set JAVA_FILES=
for /r "%SRC_DIR%\me\fuzzi\dot\launcher" %%f in (*.java) do (
    set JAVA_FILES=!JAVA_FILES! "%%f"
)

rem Проверяем, есть ли файлы для компиляции
if "!JAVA_FILES!"=="" (
    echo No Java files found to compile.
    pause
    exit /b 1
)

rem Сообщаем о начале компиляции
echo Compiling the following Java files: !JAVA_FILES!

rem Компилируем Java файлы и показываем все ошибки и предупреждения
javac -Xlint:deprecation -d "%PROJECT_DIR%source\bin" -cp "%LIB_DIR%\*" !JAVA_FILES!

rem Проверяем, была ли компиляция успешной
if errorlevel 1 (
    echo Errors during compilation!
    pause
    exit /b 1
)

rem Сообщаем о начале выполнения программы
echo Running the program...

rem Запускаем программу, устанавливая рабочую директорию на PROJECT_DIR
cd /d "%PROJECT_DIR%source"
java -cp "bin;%LIB_DIR%\*" %MAIN_CLASS%

rem Пауза перед закрытием консоли
pause

endlocal