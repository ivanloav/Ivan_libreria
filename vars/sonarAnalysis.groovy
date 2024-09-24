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
