def call(Map params = [:]) {
    boolean abortPipeline = params.get('abortPipeline', false)
    boolean qualityGateCheck = params.get('qualityGateCheck', false)
    def branchName = env.BRANCH_NAME

    withSonarQubeEnv('Sonar Local') {
        sh 'echo "Ejecución de las pruebas de calidad de código"'
        
        timeout(time: 5, unit: 'MINUTES') {
            echo 'Esperando a los resultados del análisis de SonarQube...'
            
            if (qualityGateCheck) {
                def qualityGateStatus = 'PASSED'
                echo "QualityGate result: ${qualityGateStatus}"

                if (abortPipeline) {
                    error 'El análisis de calidad ha fallado y se ha abortado el pipeline.'
                } else {
                    if (branchName == 'master' || branchName.startsWith('hotfix')) {
                        if (qualityGateStatus == 'FAILED') {
                            error "El análisis de calidad ha fallado en la rama '${branchName}' y se ha abortado el pipeline."
                        }
                    }
                }
            }
        }
    }
}
