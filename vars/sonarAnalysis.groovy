def call(Map params = [:]) {
    boolean abortPipeline = params.get('abortPipeline', false)
    boolean qualityGateCheck = params.get('qualityGateCheck', false)

    withSonarQubeEnv('Sonar Local') {
        // Ejecución del análisis de SonarQube usando Docker
        sh '''
            docker run --rm \
            -e SONAR_HOST_URL="http://192.168.1.40:9000" \
            -e SONAR_LOGIN="sqp_c895d1c73c8eea74bf1fe9d434189420d48eea4b" \
            -v $(pwd):/usr/src \
            sonarsource/sonar-scanner-cli \
            -Dsonar.projectKey=practica_1_2024_IVAN \
            -Dsonar.sources=/usr/src \
            -Dsonar.host.url=http://192.168.1.40:9000 \
            -Dsonar.login=sqp_c895d1c73c8eea74bf1fe9d434189420d48eea4b
        '''

        // Timeout de 5 minutos esperando resultados de SonarQube
        timeout(time: 5, unit: 'MINUTES') {
            echo 'Esperando a los resultados del análisis de SonarQube...'

        def qg = waitForQualityGate()

        if (qg.status != 'OK' && abortPipeline) {
            error "Quality Gate status: ${qg.status}. Pipeline will be aborted."
        }

        }
    }
}


/*
def call(Map params = [:]) {
    boolean abortPipeline = params.get('abortPipeline', false)
    boolean qualityGateCheck = params.get('qualityGateCheck', false)

    withSonarQubeEnv('Sonar Local') {
        sh 'echo "Ejecución de las pruebas de calidad de código"'
        
        timeout(time: 5, unit: 'MINUTES') {
            echo 'Esperando a los resultados del análisis de SonarQube...'
            if (qualityGateCheck) {
                def qualityGateStatus = 'PASSED'
                echo "QualityGate result: ${qualityGateStatus}"
                if (qualityGateStatus == 'FAILED' && abortPipeline) {
                    error 'El análisis de calidad ha fallado y se ha abortado el pipeline.'
                }
            }
        }
    }
}
*/