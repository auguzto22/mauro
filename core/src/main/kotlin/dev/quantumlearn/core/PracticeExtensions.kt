package dev.quantumlearn.core

internal fun reinforcement(id: String, n: Int): Practice.Draft? = when (id) {
    "algebra" -> exercise("Para x = $n, calcule 3x + 2x.", "${5 * n}",
        "Junte os termos semelhantes em 5x.", "Substitua x por $n.")
    "functions" -> exercise("Calcule a saída de g(x) = 3x − 1 na entrada x = $n.", "${3 * n - 1}",
        "Substitua x por $n.", "Multiplique por 3 e subtraia 1.")
    "trigonometry" -> exercise("Um cateto oposto mede $n m e a hipotenusa mede ${n + 4} m. Qual é o seno do ângulo?",
        "$n/${n + 4}", "Identifique o lado oposto ao ângulo.", "Divida o cateto pela hipotenusa.")
    "logarithms" -> exercise("Qual é log₁₀(10^$n)?", "$n",
        "O logaritmo pede o expoente que produz o argumento.", "A potência já informa o expoente $n.")
    "vectors" -> exercise("Some os vetores ($n, 2) e (−1, 3). Qual é a componente horizontal da soma?",
        "${n - 1}", "Pegue as duas primeiras componentes.", "Some $n e −1 sem misturar o eixo vertical.")
    "complex" -> exercise("Calcule |$n + 2i|².", "${n * n + 4}",
        "O quadrado do módulo soma os quadrados das componentes.", "Some $n² e 2².")
    "limits" -> exercise("Qual é o limite de (${n}x + 2) quando x tende a 2?", "${2 * n + 2}",
        "A função é linear e contínua no ponto.", "Use x = 2 na expressão.")
    "derivatives" -> exercise("Qual é f′($n) se f(x) = 3x²?", "${6 * n}",
        "Derive o quadrado para obter f′(x) = 6x.", "Avalie a derivada em $n.")
    "integrals" -> exercise("Calcule a integral da função constante f(x) = $n de x = 1 até x = 4.", "${3 * n}",
        "O intervalo tem largura 4 − 1 = 3.", "Multiplique a largura pela altura $n.")
    "probability" -> exercise("Há $n fichas azuis e 2 brancas, com sorteio uniforme. Qual é a probabilidade de sair azul?",
        "$n/${n + 2}", "Conte as $n fichas favoráveis.", "Divida pelo total de ${n + 2} fichas.")
    "linear-algebra" -> exercise("Em uma base, um vetor tem coordenadas ($n, −2). Qual é o coeficiente do primeiro vetor da base?",
        "$n", "A ordem das coordenadas segue a ordem dos vetores da base.", "Leia a primeira coordenada.")
    "matrices" -> exercise("Uma linha de A é ($n, −1). Calcule a saída dessa linha aplicada à coluna (2, 3).",
        "${2 * n - 3}", "Multiplique $n por 2 e −1 por 3.", "Some os dois produtos.")
    "differential-equations" -> exercise("Se y′ = −2y e y(0) = $n, qual é y′(0)?", "${-2 * n}",
        "Avalie a equação diferencial no instante zero.", "Substitua y(0) por $n.")
    "fourier" -> exercise("Um sinal tem $n tons puros distintos de frequência positiva. Quantas linhas positivas há no espectro ideal?",
        "$n", "Conte apenas as frequências positivas.", "Cada tom puro fornece uma linha positiva distinta.")
    "motion" -> exercise("Uma pessoa se desloca ${3 * n} m em 3 s para a direita. Calcule a velocidade média com unidade.",
        "$n", "Use deslocamento dividido pelo intervalo.", "Divida ${3 * n} m por 3 s.", "m/s")
    "forces" -> exercise("Calcule a força resultante de acelerar $n kg a 2 m/s². Inclua a unidade.",
        "${2 * n}", "Aplique a segunda lei de Newton.", "Multiplique $n kg por 2 m/s².", "N")
    "energy" -> exercise("Qual é a energia cinética de uma massa de $n kg a 2 m/s? Inclua a unidade.",
        "${2 * n}", "A rapidez ao quadrado vale 4 m²/s².", "Multiplique pela massa e divida por dois.", "J")
    "momentum" -> exercise("Um carrinho de $n kg move-se a −3 m/s em um eixo. Informe o valor do momento nesse eixo em kg·m/s.",
        "${-3 * n}", "Use p = mv para a componente.", "Multiplique $n por −3 e preserve o sinal.")
    "electricity" -> exercise("Um resistor de 2 Ω conduz corrente com tensão de ${2 * n} V. Calcule a corrente e inclua a unidade.",
        "$n", "Isole a corrente como V/R.", "Divida ${2 * n} volts por 2 ohms.", "A")
    "magnetism" -> exercise("A rapidez de uma partícula se torna $n vezes maior, mantendo fixos campo e ângulo de 90°. Por qual fator muda sua força magnética?",
        "$n", "Com o ângulo e o campo fixos, a força é proporcional à rapidez.", "O fator de mudança é $n.")
    "electromagnetic-fields" -> exercise("Com c ≈ 3e8 m/s, calcule f para luz no vácuo com comprimento de onda ${3 * n} m. Inclua a unidade.",
        "1e8/$n", "Isole a frequência em f = c/λ.", "Divida 3e8 m/s por ${3 * n} m.", "Hz")
    "blackbody" -> exercise("Um corpo negro tem temperatura absoluta multiplicada por $n. Por qual fator se multiplica o comprimento de onda do pico?",
        "1/$n", "Use λₚ = b/T.", "A mudança em T entra no denominador, então o fator é 1/$n.")
    "photoelectric" -> exercise("Um fóton de ${n + 2} eV incide num material com função trabalho de 2 eV. Calcule K máximo com unidade.",
        "$n", "Subtraia a função trabalho da energia do fóton.", "${n + 2} eV − 2 eV = $n eV.", "eV")
    "duality" -> exercise("O momento de uma componente de matéria é multiplicado por $n. Qual fator multiplica seu comprimento de onda?",
        "1/$n", "h é constante em λ = h/p.", "Multiplicar p por $n divide λ pelo mesmo fator.")
    "double-slit" -> exercise("Em unidades compatíveis, duas contribuições chegam em fase com amplitudes reais $n e 1. Qual é o quadrado da amplitude total?",
        "${(n + 1) * (n + 1)}", "Some primeiro as amplitudes, obtendo ${n + 1}.", "Eleve a amplitude total ao quadrado.")
    "uncertainty" -> exercise("Se Δx cresce por um fator $n, por qual fator se multiplica o limite inferior de Δp?",
        "1/$n", "O limite inferior vem de ℏ/(2Δx).", "O crescimento do denominador divide o limite por $n.")
    "wave-function" -> exercise("Uma amplitude real local vale −$n/10 em unidades compatíveis. Qual é o valor numérico de |ψ|²?",
        "${n * n}/100", "Pegue o módulo $n/10.", "Eleve ao quadrado o numerador e o denominador.")
    "born" -> exercise("Um resultado discreto tem amplitude √($n/10). Qual é a sua probabilidade?",
        "$n/10", "A amplitude é real e não negativa.", "Eleve a raiz ao quadrado e recupere $n/10.")
    "superposition" -> exercise("Um estado normalizado tem $n amplitudes não nulas de mesmo módulo. Qual é a probabilidade de um desses resultados na própria base?",
        "1/$n", "Os $n resultados têm probabilidades iguais pela regra de Born.", "Divida a probabilidade total 1 por $n.")
    "quantum-states" -> exercise("Calcule a norma de ($n, $n).", "$n*sqrt(2)",
        "Some o quadrado de cada uma das duas componentes.", "Tire a raiz de 2 × $n².")
    "operators" -> exercise("O operador X troca as amplitudes de (0, 1) em cada aplicação. Qual é o primeiro componente após $n aplicações?",
        "${n % 2}", "Cada par de aplicações recupera o vetor anterior.", "Se sobrar uma troca, a primeira componente é 1. Caso contrário, é 0.")
    "eigenvalues" -> exercise("Para um vetor não nulo v, Bv = −${n}v. Informe o autovalor.", "-$n",
        "O fator que multiplica v é o autovalor.", "Leia o fator com seu sinal.")
    "dirac" -> exercise("Um vetor não normalizado é |s⟩ = ${n}|0⟩. Qual é ⟨s|s⟩?", "${n * n}",
        "O bra ⟨s| carrega o mesmo coeficiente real $n.", "Multiplique $n × $n e use ⟨0|0⟩ = 1.")
    "schrodinger-time" -> exercise("Um autoestado evolui por uma fase exp(−iEt/ℏ). Se sua densidade inicial vale $n em um ponto, qual é seu valor numérico ali depois?",
        "$n", "A evolução multiplica a amplitude por um fator de módulo um.", "O módulo quadrado permanece igual.")
    "schrodinger-stationary" -> exercise("Para uma caixa infinita, calcule Eₙ/E₁ com n = $n.", "${n * n}",
        "A razão de energias é n².", "Substitua n por $n e eleve ao quadrado.")
    "tunneling" -> exercise("Para uma barreira sem absorção, a transmissão vale $n/10. Qual é a probabilidade de reflexão?",
        "${10 - n}/10", "Em espalhamento unidimensional sem perdas temos R + T = 1.", "Subtraia a transmissão de 1.")
    "spin" -> {
        val angle = (n - 2) * 90
        exercise("Prepare spin +z e meça num eixo a $angle°. Qual é a probabilidade de +ℏ/2?",
            listOf("1", "1/2", "0")[n - 2], "Divida o ângulo entre os eixos por dois.",
            "Calcule o quadrado do cosseno do meio-ângulo.")
    }
    "entanglement" -> exercise("Um par tem amplitudes √($n/10) para 00 e √(${10 - n}/10) para 11. Qual é a probabilidade conjunta de 11?",
        "${10 - n}/10", "Selecione a amplitude associada a 11.", "Eleve o módulo dessa raiz ao quadrado.")
    "decoherence" -> exercise("Num modelo de desfasagem, a coerência é 1/2 e a sobreposição real do ambiente é 1/$n. Qual é a nova coerência?",
        "1/${2 * n}", "A sobreposição do ambiente multiplica a coerência inicial.", "Multiplique as frações 1/2 e 1/$n.")
    "oscillator" -> exercise("A energia de ponto zero vale ℏω/2. Qual é Eₙ em unidades de ℏω para n = $n?",
        "${2 * n + 1}/2", "Some n + 1/2 mantendo o termo de ponto zero.", "Substitua n por $n.")
    "hydrogen" -> exercise("No modelo Eₙ = −13,6 eV/n², calcule a energia do nível n = $n e inclua a unidade.",
        "-13.6/${n * n}", "Calcule o denominador n² com n = $n.", "Divida −13,6 eV por esse resultado.", "eV")
    "perturbation" -> exercise("Uma energia isolada de $n eV tem correção de primeira ordem +0,3 eV. Calcule a energia aproximada com unidade.",
        "$n+0.3", "Escreva a aproximação como energia original mais correção.", "Some +0,3 eV à energia inicial.", "eV")
    "quantum-fields" -> exercise("Um modo bosônico livre ganha $n quanta. Qual é o acréscimo em unidades de ℏω?",
        "$n", "Cada excitação acrescenta uma unidade de ℏω.", "Multiplique por $n quanta.")
    "qubit" -> {
        val angle = (n - 2) * 90
        exercise("Um qubit puro tem ângulo polar θ = $angle°. Qual é a probabilidade de medir 1 na base computacional?",
            listOf("0", "1/2", "1")[n - 2], "P(1) é o quadrado do seno de θ/2.",
            "Avalie o seno para metade de $angle° e eleve ao quadrado.")
    }
    else -> null
}

private fun exercise(prompt: String, result: String, first: String, second: String, unit: String? = null) =
    Practice.Draft(prompt, result, first, second, ErrorKind.Formula, "", null, "", unit)
