@echo off
echo === DESPLIEGUE XAFRA-ADS EN GOOGLE CLOUD PLATFORM ===

echo.
echo [1/5] Verificando configuracion de gcloud...
call gcloud config get-value project
if not "%ERRORLEVEL%"=="0" (
    echo ERROR: gcloud no esta configurado correctamente
    echo Ejecuta: gcloud auth login
    echo Y luego: gcloud config set project xafra-ads
    pause
    exit /b 1
)

echo.
echo [2/5] Compilando aplicacion...
call C:\tools\apache-maven-3.8.8\bin\mvn.cmd clean package -DskipTests
if not "%ERRORLEVEL%"=="0" (
    echo ERROR: Fallo la compilacion
    pause
    exit /b 1
)

echo.
echo [3/5] Verificando WAR generado...
if not exist "target\ads-xafra.war" (
    echo ERROR: No se encontro el archivo WAR
    pause
    exit /b 1
)

echo.
echo [4/5] Configurando proyecto GCP...
call gcloud config set project xafra-ads

echo.
echo [5/5] Desplegando en App Engine...
call gcloud app deploy src/main/appengine/app.yaml --quiet

if "%ERRORLEVEL%"=="0" (
    echo.
    echo === DESPLIEGUE EXITOSO ===
    echo.
    echo La aplicacion esta disponible en:
    echo https://xafra-ads.uc.r.appspot.com
    echo.
    echo Endpoints disponibles:
    echo - https://xafra-ads.uc.r.appspot.com/v1/ping
    echo - https://xafra-ads.uc.r.appspot.com/v1/config/countries
    echo - https://xafra-ads.uc.r.appspot.com/v1/config/costa-rica-kolbi
    echo - https://xafra-ads.uc.r.appspot.com/api/config/xafra
    echo.
) else (
    echo.
    echo ERROR: Fallo el despliegue
    echo Revisa los logs con: gcloud app logs tail -s default
)

pause
