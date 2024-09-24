def call(boolean abortPipeline = false, boolean qualityGateCheck = false) {
    withSonarQubeEnv('Sonar Local') {
        sh 'echo "Ejecución de las pruebas de calidad de código"'
        
        timeout(time: 5, unit: 'MINUTES') {
            echo 'Esperando a los resultados del análisis de SonarQube...'
            if (qualityGateCheck) {
                def qualityGateStatus = 'FAILED'
                echo "QualityGate result: ${qualityGateStatus}"
                if (qualityGateStatus == 'FAILED' && abortPipeline) {
                    error 'El análisis de calidad ha fallado y se ha abortado el pipeline.'
                }
            }
        }
    }
}
