package dev.quantumlearn.core

internal fun mathGuides(): List<FormulaGuide> = listOf(
    guide("operations", """a+b\times c""", "a + b × c",
        "a mais b vezes c. Primeiro o produto b × c, depois a soma com a.",
        listOf(sym("a", "a quantidade solta, somada no fim"), sym("b", "o número de grupos iguais"),
            sym("c", "quantos itens há em cada grupo")),
        listOf(
            step("""2+3\times4""", "Olhe a expressão inteira antes de calcular. Há uma soma e um produto."),
            step("""2+(3\times4)""", "A multiplicação funciona como se tivesse parênteses invisíveis: ela é resolvida antes."),
            step("""2+12""", "Três grupos de quatro dão 12 itens."),
            step("""14""", "Só agora junte a parte solta. Se você somasse primeiro, obteria 20, que é outra conta: (2 + 3) × 4.")
        ),
        listOf(
            real(Scene.Money, "No mercado", "Você leva 3 iogurtes de R\$ 4 cada e uma sacola de R\$ 2. O caixa calcula o produto antes de somar.",
                "2 + 3 × 4 = 2 + 12 = R\$ 14",
                plug("Você compra 5 cadernos de R\$ 7 e paga R\$ 3 de entrega. Quanto paga no total, em reais?", "38",
                    "Monte a expressão 3 + 5 × 7.", "Calcule 5 × 7 = 35 e some 3. Total: R\$ 38.",
                    trap = "56", trapWhy = "56 vem de (3 + 5) × 7. A taxa de entrega é cobrada uma vez só, não em cada caderno.")),
            real(Scene.Phone, "Plano de internet", "Um plano dá 10 GB fixos e mais 2 GB de bônus por semana. Em 4 semanas, o total segue a mesma estrutura.",
                "10 + 4 × 2 = 18 GB",
                plug("Seu plano tem 15 GB fixos e 3 GB extras por semana. Quantos GB você terá em 4 semanas?", "27",
                    "Monte 15 + 4 × 3.", "Calcule 4 × 3 = 12 e some 15. Total: 27 GB.",
                    trap = "72", trapWhy = "72 vem de (15 + 4) × 3. Os 15 GB fixos não se repetem a cada semana."))
        ),
        iso("Em a + b × c, qual parte se calcula primeiro?", "b × c",
            "A multiplicação tem prioridade sobre a soma quando não há parênteses.",
            "a + b" to "Somar primeiro equivale a escrever (a + b) × c, que é outra expressão.",
            "a × c" to "a não participa do produto. Ele é somado no fim.")
    ) { a, b ->
        val c = (a + b) % 5 + 2
        plug("Use a + b × c com a = $a, b = $b e c = $c.", "${a + b * c}",
            "Substitua: $a + $b × $c.", "Calcule $b × $c = ${b * c} e some $a. Resultado: ${a + b * c}.",
            trap = "${(a + b) * c}", trapWhy = "Você somou $a + $b antes de multiplicar. Sem parênteses, o produto vem primeiro.")
    },

    guide("fractions", """\frac{a\cdot k}{b\cdot k}=\frac{a}{b}""", "(a·k)/(b·k) = a/b",
        "Se em cima e embaixo há o mesmo fator k, ele pode ser cancelado sem mudar o valor.",
        listOf(sym("a", "o numerador, as partes escolhidas"), sym("b", "o denominador, em quantas partes o todo foi dividido"),
            sym("k", "o fator comum aos dois termos, diferente de zero")),
        listOf(
            step("""\frac{6}{8}""", "Procure um número que divida os dois termos ao mesmo tempo. Aqui, 2 divide 6 e 8."),
            step("""\frac{6\div2}{8\div2}""", "Divida em cima e embaixo pelo mesmo fator. Assim a proporção não muda."),
            step("""\frac{3}{4}""", "3 e 4 não têm divisor comum além de 1. A fração está irredutível."),
            step("""\frac{3}{4}=0{,}75""", "A barra também é uma divisão. Frações equivalentes dão o mesmo decimal.")
        ),
        listOf(
            real(Scene.Home, "Na pizza", "Uma pizza grande tem 8 fatias. Comer 2 fatias é o mesmo que comer 1/4 da pizza.",
                "2/8 = (1·2)/(4·2) = 1/4",
                plug("Uma pizza tem 12 fatias e você comeu 4. Que fração irredutível da pizza você comeu?", "1/3",
                    "Escreva 4/12. O fator comum é 4.", "Divida 4 ÷ 4 = 1 e 12 ÷ 4 = 3. Resultado: 1/3.",
                    trap = "4/3", trapWhy = "Você dividiu só o denominador. O fator precisa sair dos dois termos.",
                    format = NumberFormat.ReducedFraction)),
            real(Scene.Home, "Na receita", "A receita pede 3/4 de xícara e você só tem um medidor de 1/4. Você enche esse medidor 3 vezes.",
                "3/4 = 3 × 1/4"),
            real(Scene.Phone, "Bateria do celular", "Com 60 de 100 pontos de carga, a bateria está em 60/100 = 3/5 da capacidade.",
                "60/100 = (3·20)/(5·20) = 3/5",
                plug("Seu celular está com 75% de bateria. Escreva 75/100 como fração irredutível.", "3/4",
                    "O fator comum de 75 e 100 é 25.", "Divida 75 ÷ 25 = 3 e 100 ÷ 25 = 4. Resultado: 3/4.",
                    format = NumberFormat.ReducedFraction))
        ),
        iso("Para simplificar 10/15, o que você deve fazer?", "Dividir 10 e 15 por 5",
            "O mesmo fator sai do numerador e do denominador, então o valor continua igual: 2/3.",
            "Dividir só o 10 por 5" to "Mexer só em cima muda a fração: 2/15 é bem menor que 10/15.",
            "Subtrair 5 dos dois" to "Subtrair muda a proporção: 5/10 = 1/2, que não é igual a 10/15.")
    ) { a, b ->
        val top = a
        val bottom = a + b
        val k = b % 3 + 2
        val g = gcd(top, bottom)
        plug("Simplifique ${top * k}/${bottom * k} até a forma irredutível.", "${top / g}/${bottom / g}",
            "O maior fator comum de ${top * k} e ${bottom * k} é ${k * g}.",
            "Divida os dois termos por ${k * g}: ${top / g}/${bottom / g}.",
            trap = "${top / g}/${bottom * k}", trapWhy = "Você dividiu só o numerador. O denominador precisa ser dividido pelo mesmo fator.",
            format = NumberFormat.ReducedFraction)
    },

    guide("powers", """a^{n}=a\times a\times…\times a""", "aⁿ = a × a × … × a (n fatores)",
        "a elevado a n: escreva a base a e multiplique n cópias dela.",
        listOf(sym("a", "a base, o fator que se repete"), sym("n", "o expoente, quantas cópias da base entram no produto")),
        listOf(
            step("""2^{3}""", "Leia: base 2, expoente 3."),
            step("""2\times2\times2""", "O expoente conta os fatores. Três cópias do 2."),
            step("""=8""", "Multiplique: 2 × 2 = 4 e 4 × 2 = 8."),
            step("""2^{3}\times2^{2}=2^{5}""", "Mesma base: junte os fatores somando os expoentes, 3 + 2 = 5. O resultado é 32.")
        ),
        listOf(
            real(Scene.Computer, "Bits e cores", "Cada bit dobra as combinações. Com 8 bits há 2⁸ = 256 valores. Por isso cada canal de cor RGB vai de 0 a 255.",
                "2⁸ = 256 valores (0 a 255)",
                plug("Um código usa 10 bits. Quantas combinações diferentes ele tem?", "1024",
                    "Cada bit tem 2 estados: 2¹⁰.", "2¹⁰ = 1024 combinações.",
                    trap = "20", trapWhy = "20 seria 2 × 10. O expoente pede 10 fatores 2 multiplicados.")),
            real(Scene.Health, "Bactérias", "Uma bactéria que se divide a cada 20 minutos vira 2, depois 4, depois 8. Em 1 hora são 3 divisões.",
                "2³ = 8 bactérias"),
            real(Scene.Money, "Senha do banco", "Uma senha de 4 dígitos tem 10 opções em cada posição.",
                "10⁴ = 10 000 combinações",
                plug("Quantas senhas diferentes de 6 dígitos (0 a 9) existem?", "1000000",
                    "Cada posição tem 10 opções: 10⁶.", "10⁶ = 1 000 000 de senhas.",
                    trap = "60", trapWhy = "60 vem de 10 × 6. O expoente indica 6 fatores 10."))
        ),
        iso("O que 5³ significa?", "5 × 5 × 5",
            "O expoente 3 conta três cópias da base 5. O valor é 125.",
            "5 × 3" to "Isso trata o expoente como fator. 5 × 3 = 15, não 125.",
            "3 × 3 × 3 × 3 × 3" to "Os papéis ficaram trocados. Isso é 3⁵ = 243.")
    ) { a, b ->
        val n = b % 3 + 2
        var power = 1
        repeat(n) { power *= a }
        plug("Calcule $a^$n.", "$power", "Escreva $n cópias da base: ${List(n) { a }.joinToString(" × ")}.",
            "Multiplique tudo: $power.", trap = "${a * n}", trapWhy = "Você multiplicou a base pelo expoente. O expoente conta quantas vezes a base se repete.")
    },

    guide("roots", """\sqrt{x}=r\quad\Leftrightarrow\quad r^{2}=x""", "√x = r ⇔ r² = x (r ≥ 0)",
        "A raiz quadrada de x é o número não negativo r que, multiplicado por ele mesmo, dá x.",
        listOf(sym("x", "o radicando, o número sob a raiz"), sym("r", "a raiz principal, sempre maior ou igual a zero")),
        listOf(
            step("""\sqrt{36}""", "A pergunta é: que número vezes ele mesmo dá 36?"),
            step("""6\times6=36""", "Teste candidatos até encontrar o quadrado certo."),
            step("""\sqrt{36}=6""", "−6 também tem quadrado 36, mas o símbolo √ escolhe a raiz não negativa."),
            step("""4<\sqrt{20}<5""", "Se não for quadrado perfeito, a raiz fica entre dois inteiros: 4² = 16 e 5² = 25. √20 ≈ 4,47.")
        ),
        listOf(
            real(Scene.Home, "Piso quadrado", "Um cômodo quadrado tem 49 m² de área. Para comprar rodapé, você precisa do lado.",
                "lado = √49 = 7 m",
                plug("Um terreno quadrado tem 144 m² de área. Quanto mede cada lado?", "12",
                    "O lado é a raiz da área: √144.", "12 × 12 = 144, então o lado mede 12 m.", unit = "m",
                    trap = "72", trapWhy = "72 é metade da área. A raiz desfaz o quadrado, não divide por dois.")),
            real(Scene.Phone, "Tamanho de tela", "A diagonal de uma TV vem de Pitágoras: √(largura² + altura²). Uma tela 48 × 27 polegadas tem diagonal de cerca de 55 polegadas.",
                "√(48² + 27²) = √3033 ≈ 55"),
            real(Scene.Sport, "Quadra", "Um campo quadrado de 400 m² tem lados de 20 m. Correr em volta dele é percorrer 4 × 20 = 80 m.",
                "√400 = 20 m",
                plug("Uma quadra quadrada tem área de 625 m². Qual o comprimento do lado?", "25",
                    "Calcule √625.", "25 × 25 = 625, então o lado mede 25 m.", unit = "m"))
        ),
        iso("Se r = √x, qual igualdade é sempre verdadeira?", "r² = x",
            "A raiz desfaz o quadrado: elevar r ao quadrado recupera x.",
            "2r = x" to "Dobrar não desfaz uma raiz. √16 = 4, mas 2 × 4 = 8, não 16.",
            "x² = r" to "Os papéis ficaram trocados. Quem é elevado ao quadrado é a raiz r.")
    ) { a, b ->
        val r = a + b
        plug("Calcule √${r * r}.", "$r", "Procure um número não negativo cujo quadrado seja ${r * r}.",
            "$r × $r = ${r * r}, então √${r * r} = $r.",
            trap = "-$r", trapWhy = "−$r também tem quadrado ${r * r}, mas o símbolo √ representa a raiz não negativa.")
    },

    guide("equations", """ax+b=c\quad\Rightarrow\quad x=\frac{c-b}{a}""", "ax + b = c ⇒ x = (c − b)/a",
        "Para achar x, desfaça as operações de fora para dentro: tire b, depois divida por a.",
        listOf(sym("x", "a incógnita, o valor que queremos descobrir"), sym("a", "o coeficiente que multiplica x"),
            sym("b", "o termo constante somado a ax"), sym("c", "o valor do outro lado da igualdade")),
        listOf(
            step("""2x+1=9""", "Pense numa balança equilibrada. O que fizer de um lado, faça do outro."),
            step("""2x=9-1=8""", "Subtraia b = 1 dos dois lados para tirar o termo solto."),
            step("""x=\frac{8}{2}=4""", "Divida os dois lados por a = 2 para desfazer a multiplicação."),
            step("""2\cdot4+1=9""", "Confira substituindo x = 4 na equação original.")
        ),
        listOf(
            real(Scene.Transport, "Corrida de aplicativo", "Uma corrida cobra R\$ 5 fixos mais R\$ 2 por km. Se a corrida custou R\$ 25, dá para descobrir a distância.",
                "2x + 5 = 25 ⇒ x = (25 − 5)/2 = 10 km",
                plug("Um táxi cobra R\$ 6 de bandeirada e R\$ 3 por km. A corrida deu R\$ 42. Quantos km?", "12",
                    "Monte 3x + 6 = 42 e subtraia 6: 3x = 36.", "Divida por 3: x = 12 km.",
                    trap = "16", trapWhy = "16 vem de somar 6 em vez de subtrair. Para cancelar +6, subtraia 6 dos dois lados.")),
            real(Scene.Sport, "Academia", "Matrícula de R\$ 30 e mensalidade de R\$ 80. Pagando R\$ 350 no total, o número de meses é a incógnita.",
                "80x + 30 = 350 ⇒ x = 320/80 = 4 meses",
                plug("Uma academia cobra matrícula de R\$ 50 e R\$ 90 por mês. Você pagou R\$ 590. Quantos meses?", "6",
                    "Monte 90x + 50 = 590 e subtraia 50: 90x = 540.", "Divida por 90: x = 6 meses.")),
            real(Scene.Phone, "Conta do celular", "Plano de R\$ 20 mais R\$ 5 por GB extra. A conta de R\$ 45 indica quantos GB extras foram usados.",
                "5x + 20 = 45 ⇒ x = 5 GB")
        ),
        iso("Em ax + b = c, qual expressão dá x?", "x = (c − b)/a",
            "Primeiro tire b dos dois lados, depois divida por a.",
            "x = (c + b)/a" to "Para cancelar +b é preciso subtrair b, não somar.",
            "x = c/a − b" to "Dividir antes de tirar b também divide o b. A ordem correta é subtrair primeiro.")
    ) { a, b ->
        val x = b
        val constant = (a + b) % 7 + 1
        val total = a * x + constant
        plug("Resolva ${a}x + $constant = $total.", "$x", "Subtraia $constant dos dois lados: ${a}x = ${a * x}.",
            "Divida por $a: x = $x.", trap = "(${total}+$constant)/$a",
            trapWhy = "Você somou $constant em vez de subtrair. Para cancelar +$constant, subtraia dos dois lados.")
    },

    guide("scientific", """N=m\times10^{k}\quad1\le m<10""", "N = m × 10ᵏ, com 1 ≤ m < 10",
        "Todo número positivo vira um coeficiente entre 1 e 10 vezes uma potência de dez.",
        listOf(sym("N", "o número escrito por extenso"), sym("m", "o coeficiente, entre 1 e 10"),
            sym("k", "o expoente inteiro, quantas casas a vírgula andou")),
        listOf(
            step("""0{,}00052""", "Ande com a vírgula até sobrar um único algarismo não nulo antes dela."),
            step("""5{,}2""", "A vírgula andou 4 casas para a direita."),
            step("""5{,}2\times10^{-4}""", "Andar para a direita deixa o número maior. O expoente −4 compensa e devolve a escala certa."),
            step("""5\,970\,000=5{,}97\times10^{6}""", "Para números grandes, a vírgula anda para a esquerda e o expoente fica positivo.")
        ),
        listOf(
            real(Scene.Space, "Massa da Terra", "A Terra tem massa de aproximadamente 5 970 000 000 000 000 000 000 000 kg. Escrito assim, ninguém conta os zeros.",
                "≈ 5,97 × 10²⁴ kg"),
            real(Scene.Health, "Células e vírus", "Um glóbulo vermelho tem cerca de 0,000008 m de diâmetro. Um vírus da gripe é quase cem vezes menor.",
                "0,000008 m = 8 × 10⁻⁶ m",
                plug("Um vírus da gripe mede cerca de 0,0000001 m. Escreva em notação científica, como 3e-5.", "1e-7",
                    "Ande com a vírgula 7 casas para a direita até obter 1.", "O expoente é −7: 1 × 10⁻⁷ m.",
                    trap = "1e7", trapWhy = "Números menores que 1 têm expoente negativo.", format = NumberFormat.Scientific)),
            real(Scene.Computer, "Armazenamento", "Um terabyte tem 1 000 000 000 000 bytes.",
                "1 TB = 10¹² bytes",
                plug("Um HD tem 2 000 000 000 000 bytes. Escreva em notação científica, como 3e5.", "2e12",
                    "Ande com a vírgula até obter 2. Ela andou 12 casas para a esquerda.", "Resultado: 2 × 10¹² bytes.",
                    trap = "20e11", trapWhy = "O valor confere, mas o coeficiente precisa ficar entre 1 e 10.",
                    format = NumberFormat.Scientific))
        ),
        iso("Quanto vale 3,1 × 10⁵ escrito por extenso?", "310 000",
            "10⁵ desloca a vírgula 5 casas para a direita: 3,1 vira 310 000.",
            "3 100 000" to "Isso seria 3,1 × 10⁶. Conte as casas a partir da vírgula.",
            "0,000031" to "Expoente positivo aumenta o número. 0,000031 corresponde a 3,1 × 10⁻⁵.")
    ) { a, b ->
        val zeros = b + 1
        plug("Escreva $a${"0".repeat(zeros)} em notação científica, como 3e5.", "${a}e$zeros",
            "O coeficiente é $a. A vírgula anda $zeros casas para a esquerda.", "Resultado: $a × 10^$zeros.",
            trap = "${a}e-$zeros", trapWhy = "Para números maiores que 10, o expoente é positivo.", format = NumberFormat.Scientific)
    },

    guide("algebra", """ax+bx=(a+b)x""", "ax + bx = (a + b)x",
        "Termos com a mesma letra são como coisas do mesmo tipo: some só os números da frente.",
        listOf(sym("x", "a parte literal, a mesma em todos os termos"), sym("a", "o coeficiente do primeiro termo"),
            sym("b", "o coeficiente do segundo termo")),
        listOf(
            step("""3x+2x""", "Os dois termos têm x. Eles são semelhantes."),
            step("""(3+2)x""", "Coloque x em evidência: é a propriedade distributiva ao contrário."),
            step("""5x""", "Some os coeficientes. A letra continua igual."),
            step("""3x+2""", "Um termo sem x não se junta com termos em x. A expressão já está simplificada.")
        ),
        listOf(
            real(Scene.Money, "Ingressos", "Você comprou 3 ingressos de manhã e 2 à tarde, todos ao mesmo preço x.",
                "3x + 2x = 5x",
                plug("Você comprou 4 ingressos de manhã e 3 à tarde, cada um a R\$ 12. Quanto gastou?", "84",
                    "Simplifique 4x + 3x = 7x.", "Substitua x = 12: 7 × 12 = R\$ 84.",
                    trap = "19", trapWhy = "19 é 7 + 12. Depois de simplificar para 7x, multiplique pelo preço.")),
            real(Scene.Home, "Caixas de piso", "Uma obra usou 4 caixas na sala e 6 no quarto, todas com x peças. O total é 10x peças.",
                "4x + 6x = 10x",
                plug("Cada caixa tem 8 peças. Foram usadas 5 caixas na cozinha e 7 no banheiro. Quantas peças?", "96",
                    "Simplifique 5x + 7x = 12x.", "Substitua x = 8: 12 × 8 = 96 peças."))
        ),
        iso("Qual expressão equivale a 7x − 2x?", "5x",
            "Os dois termos têm x. Subtraia os coeficientes: 7 − 2 = 5.",
            "5" to "A letra não desaparece. O resultado ainda conta x.",
            "5x²" to "x² surgiria de x · x. Aqui os termos são subtraídos, não multiplicados.")
    ) { a, b ->
        val x = (a * b) % 5 + 2
        plug("Simplifique ${a}x + ${b}x e calcule o valor para x = $x.", "${(a + b) * x}",
            "Some os coeficientes: ${a}x + ${b}x = ${a + b}x.", "Substitua x = $x: ${a + b} × $x = ${(a + b) * x}.",
            trap = "${a + b}", trapWhy = "${a + b} é só o coeficiente. Depois de simplificar, troque x por $x.")
    },

    guide("functions", """f(x)=mx+n""", "f(x) = mx + n",
        "f de x é igual a m vezes x mais n: cada entrada x gera uma única saída.",
        listOf(sym("f(x)", "a saída da função"), sym("x", "a entrada"), sym("m", "a taxa de variação, quanto f muda a cada +1 em x"),
            sym("n", "o valor inicial, f quando x = 0")),
        listOf(
            step("""f(x)=2x+4""", "A regra diz o que fazer com qualquer entrada."),
            step("""f(3)=2\cdot3+4""", "Troque cada x pela entrada 3."),
            step("""f(3)=10""", "Calcule: 6 + 4 = 10."),
            step("""f(4)-f(3)=2""", "Cada passo de +1 em x soma m = 2 à saída. É a inclinação da reta.")
        ),
        listOf(
            real(Scene.Transport, "Táxi", "Bandeirada de R\$ 5 mais R\$ 3 por km. O preço é uma função da distância.",
                "f(x) = 3x + 5 ⇒ f(8) = R\$ 29",
                plug("Com bandeirada de R\$ 5 e R\$ 3 por km, quanto custa uma corrida de 12 km?", "41",
                    "Monte f(x) = 3x + 5 e use x = 12.", "3 × 12 = 36. Some 5: R\$ 41.",
                    trap = "36", trapWhy = "36 é só a parte variável. Falta somar a bandeirada de R\$ 5.")),
            real(Scene.Phone, "Bateria", "Um celular começa em 100% e perde 8% por hora de uso intenso.",
                "f(t) = 100 − 8t ⇒ f(5) = 60%",
                plug("Um celular começa com 100% e perde 12% por hora. Quanto resta depois de 5 horas, em %?", "40",
                    "Monte f(t) = 100 − 12t e use t = 5.", "12 × 5 = 60. 100 − 60 = 40%."))
        ),
        iso("Em f(x) = mx + n, quanto vale f(0)?", "n",
            "Com x = 0 o termo mx some. Sobra n, o valor inicial.",
            "m" to "m é a inclinação. Ele multiplica x, que vale zero.",
            "0" to "A função só passa pela origem se n = 0.")
    ) { a, b ->
        val x = (a + b) % 6 + 1
        plug("Para f(x) = ${a}x + $b, calcule f($x).", "${a * x + b}",
            "Troque x por $x: $a × $x + $b.", "Calcule ${a * x} + $b = ${a * x + b}.",
            trap = "${a * (x + b)}", trapWhy = "Você somou $b antes de multiplicar. Primeiro m × x, depois + n.")
    },

    guide("trigonometry", """\sin\ \theta=\frac{o}{h}\quad\cos\ \theta=\frac{a}{h}""", "sen θ = o/h · cos θ = a/h",
        "Seno é o cateto oposto sobre a hipotenusa. Cosseno é o cateto adjacente sobre a hipotenusa.",
        listOf(sym("θ", "o ângulo agudo analisado", "graus"), sym("o", "o cateto oposto ao ângulo"),
            sym("a", "o cateto adjacente ao ângulo"), sym("h", "a hipotenusa, o lado maior")),
        listOf(
            step("""h""", "Ache a hipotenusa: é o lado em frente ao ângulo reto e o maior do triângulo."),
            step("""o""", "O cateto oposto fica do outro lado de θ, sem tocar nele."),
            step("""\sin\ \theta=\frac{o}{h}""", "A razão não depende do tamanho do triângulo, só do ângulo."),
            step("""o=h\cdot\sin\ \theta""", "Isolando, a altura vira comprimento vezes seno. É a forma mais usada na prática.")
        ),
        listOf(
            real(Scene.Transport, "Rampas e escadas", "Uma rampa de 10 m inclinada 30° sobe 5 m, porque sen 30° = 1/2.",
                "altura = 10 × sen 30° = 5 m",
                plug("Uma escada de 6 m forma 30° com o chão. A que altura ela toca a parede? Use sen 30° = 1/2.", "3",
                    "A escada é a hipotenusa e a altura é o cateto oposto: o = 6 × sen 30°.", "6 × 1/2 = 3 m.", unit = "m",
                    trap = "12", trapWhy = "12 vem de 6 ÷ 1/2. Para achar o cateto, multiplique a hipotenusa pelo seno.")),
            real(Scene.Nature, "Sombra de um prédio", "Com o sol a 45°, a sombra tem o mesmo comprimento que a altura do prédio, porque tg 45° = 1.",
                "altura = sombra × tg 45°"),
            real(Scene.Phone, "Jogos e GPS", "Para mover um personagem na diagonal, o jogo decompõe a velocidade em x = v·cos θ e y = v·sen θ.",
                "x = v cos θ · y = v sen θ",
                plug("Num jogo, um personagem corre a 10 m/s a 60° do eixo x. Qual a componente x da velocidade? Use cos 60° = 1/2.", "5",
                    "A componente x é v × cos θ.", "10 × 1/2 = 5 m/s.", unit = "m/s",
                    trap = "20", trapWhy = "Multiplique pelo cosseno em vez de dividir."))
        ),
        iso("Sabendo h e θ, como achar o cateto oposto?", "o = h · sen θ",
            "Multiplique os dois lados de sen θ = o/h por h.",
            "o = h / sen θ" to "Dividir pelo seno daria um valor maior que a hipotenusa.",
            "o = h · cos θ" to "O cosseno dá o cateto adjacente.")
    ) { a, b ->
        val h = 2 * a
        val useSine = b % 2 == 0
        val ratio = if (useSine) "sen 30°" else "cos 60°"
        plug("Num triângulo retângulo, h = $h m. Calcule o cateto ${if (useSine) "oposto a 30°" else "adjacente a 60°"}. Use $ratio = 1/2.",
            "$a", "O cateto é h × $ratio = $h × 1/2.", "Resultado: $a m.", unit = "m",
            trap = "${h * 2}", trapWhy = "Você dividiu pela razão. Para achar o cateto, multiplique a hipotenusa.")
    },

    guide("logarithms", """\log_{b}(x)=y\quad\Leftrightarrow\quad b^{y}=x""", "log_b(x) = y ⇔ bʸ = x",
        "O logaritmo responde: a que expoente devo elevar a base b para chegar em x?",
        listOf(sym("b", "a base, positiva e diferente de 1"), sym("x", "o argumento, o número que queremos alcançar"),
            sym("y", "o resultado do logaritmo, o expoente procurado")),
        listOf(
            step("""\log_{2}(32)""", "Pergunta: 2 elevado a quanto dá 32?"),
            step("""2\times2\times2\times2\times2=32""", "Conte as multiplicações: são 5."),
            step("""\log_{2}(32)=5""", "O logaritmo é a contagem de fatores."),
            step("""\log(ab)=\log a+\log b""", "Multiplicar vira somar. É por isso que escalas log comprimem números enormes.")
        ),
        listOf(
            real(Scene.Music, "Decibéis", "Cada +10 dB significa intensidade 10 vezes maior. Um show a 110 dB é 10⁵ vezes mais intenso que uma conversa a 60 dB.",
                "ΔdB = 10 log(I₂/I₁)",
                plug("Um som de 90 dB tem intensidade quantas vezes maior que um de 60 dB?", "1000",
                    "A diferença é 30 dB, isto é, 3 passos de 10 dB.", "Cada passo multiplica por 10: 10³ = 1000.",
                    trap = "30", trapWhy = "30 é a diferença em dB. Cada 10 dB multiplica a intensidade por 10.")),
            real(Scene.Lab, "pH", "pH = −log[H⁺]. Um suco de limão com pH 2 tem 10⁵ vezes mais íons H⁺ que a água pura, com pH 7.",
                "10^(7 − 2) = 100 000",
                plug("Um vinagre tem pH 3 e a água tem pH 7. Quantas vezes mais H⁺ há no vinagre?", "10000",
                    "A diferença de pH é 4.", "Cada unidade de pH vale um fator 10: 10⁴ = 10 000.")),
            real(Scene.Nature, "Terremotos", "Na escala de magnitude, cada ponto a mais libera cerca de 32 vezes mais energia.",
                "energia ∝ 10^(1,5 M)")
        ),
        iso("log₂(x) = 4 significa:", "2⁴ = x",
            "O logaritmo é o expoente da base. Então x = 16.",
            "4² = x" to "A base é 2, não 4. 4² = 16 por coincidência, mas a leitura está trocada.",
            "x⁴ = 2" to "x é o resultado da potência, não a base.")
    ) { a, b ->
        val n = b % 4 + 1
        var power = 1
        repeat(n) { power *= a }
        plug("Calcule log_$a($power).", "$n", "Pergunte: $a elevado a quanto dá $power?",
            "${List(n) { a }.joinToString(" × ")} = $power. O expoente é $n.",
            trap = "${power / a}", trapWhy = "O logaritmo é o expoente, não o resultado de uma divisão.")
    },

    guide("vectors", """|v|=\sqrt{v_{x}^{2}+v_{y}^{2}}""", "|v| = √(vₓ² + vᵧ²)",
        "O comprimento de um vetor vem de Pitágoras com as suas duas componentes.",
        listOf(sym("v", "o vetor, com direção, sentido e tamanho"), sym("vₓ", "a componente horizontal"),
            sym("vᵧ", "a componente vertical"), sym("|v|", "o módulo, o comprimento do vetor")),
        listOf(
            step("""v=(3,4)""", "3 passos para a direita e 4 para cima."),
            step("""3^{2}+4^{2}=25""", "As componentes são catetos de um triângulo retângulo."),
            step("""|v|=\sqrt{25}=5""", "O módulo é a hipotenusa: a distância em linha reta."),
            step("""(1,2)+(3,1)=(4,3)""", "Para somar vetores, some eixo com eixo.")
        ),
        listOf(
            real(Scene.Phone, "GPS e drones", "Andar 3 km para leste e 4 km para norte deixa você a 5 km do ponto inicial em linha reta.",
                "√(3² + 4²) = 5 km",
                plug("Um drone voa 6 m para leste e 8 m para norte. A que distância está do ponto de partida?", "10",
                    "Some os quadrados: 6² + 8² = 100.", "√100 = 10 m.", unit = "m",
                    trap = "14", trapWhy = "14 é 6 + 8. Componentes perpendiculares se combinam por Pitágoras.")),
            real(Scene.Transport, "Avião com vento", "Um avião a 300 km/h para norte com vento de 40 km/h para leste tem velocidade real de √(300² + 40²) ≈ 302,7 km/h.",
                "(0, 300) + (40, 0) = (40, 300)"),
            real(Scene.Sport, "Chute", "A velocidade da bola se divide em uma parte que avança e outra que sobe.",
                "v = (vₓ, vᵧ)",
                plug("Uma bola sai com componentes de 9 m/s para frente e 12 m/s para cima. Qual o módulo da velocidade?", "15",
                    "Some 9² + 12² = 81 + 144 = 225.", "√225 = 15 m/s.", unit = "m/s"))
        ),
        iso("Quanto vale (2, 5) + (4, −1)?", "(6, 4)",
            "Some horizontal com horizontal: 2 + 4 = 6. Vertical com vertical: 5 − 1 = 4.",
            "(7, 3)" to "Os eixos foram misturados. Some componentes do mesmo eixo.",
            "(6, 6)" to "O sinal de −1 foi ignorado. 5 + (−1) = 4.")
    ) { a, b ->
        val (x, y, c) = triples[(a + b) % triples.size]
        plug("Qual o módulo do vetor ($x, $y)?", "$c", "Some os quadrados: $x² + $y² = ${c * c}.",
            "√${c * c} = $c.", trap = "${x + y}", trapWhy = "Somar as componentes não dá o comprimento. Use Pitágoras.")
    },

    guide("complex", """|z|=\sqrt{a^{2}+b^{2}}""", "z = a + bi · |z| = √(a² + b²)",
        "Um número complexo é um ponto no plano. Seu módulo é a distância até a origem.",
        listOf(sym("z", "o número complexo"), sym("a", "a parte real, eixo horizontal"),
            sym("b", "a parte imaginária, eixo vertical"), sym("i", "a unidade imaginária, com i² = −1")),
        listOf(
            step("""z=3+4i""", "Desenhe o ponto (3, 4) no plano complexo."),
            step("""|z|^{2}=3^{2}+4^{2}=25""", "Use Pitágoras com as partes real e imaginária."),
            step("""|z|=5""", "A raiz dá o comprimento da seta."),
            step("""(a+bi)(a-bi)=a^{2}+b^{2}""", "Multiplicar pelo conjugado cancela i e explica por que |z|² é real e positivo.")
        ),
        listOf(
            real(Scene.Phone, "Sinal 4G e 5G", "Cada símbolo transmitido pelo celular é um ponto a + bi. A distância à origem indica a potência do sinal.",
                "potência ∝ |z|²",
                plug("Um símbolo 5G vale 3 + 4i em unidades arbitrárias. Qual é a potência |z|²?", "25",
                    "Some os quadrados: 3² + 4².", "9 + 16 = 25.",
                    trap = "5", trapWhy = "5 é o módulo |z|. A potência pede o quadrado do módulo.")),
            real(Scene.Home, "Rede elétrica", "Engenheiros representam tensões alternadas como números complexos, chamados fasores, para somar ondas com fases diferentes.",
                "V = |V|·(cos φ + i sen φ)"),
            real(Scene.Lab, "Mecânica quântica", "Amplitudes quânticas são complexas. A probabilidade de um resultado é o quadrado do módulo.",
                "P = |a + bi|² = a² + b²",
                plug("Uma amplitude vale 0,6i. Qual a probabilidade |z|²?", "0.36",
                    "A parte real é 0 e a imaginária é 0,6.", "0² + 0,6² = 0,36.",
                    trap = "-0.36", trapWhy = "(0,6i)² = −0,36, mas o módulo ao quadrado usa a² + b², sempre positivo."))
        ),
        iso("|a + bi|² é igual a:", "a² + b²",
            "É Pitágoras no plano complexo. O conjugado cancela os termos com i.",
            "a² − b²" to "a² − b² é a parte real de (a + bi)², que é outra conta.",
            "(a + b)²" to "Elevar a soma ao quadrado cria um termo 2ab que não pertence ao módulo.")
    ) { a, b ->
        val (x, y, c) = triples[(a * b) % triples.size]
        plug("Calcule |$x + ${y}i|.", "$c", "Some os quadrados: $x² + $y² = ${c * c}.", "√${c * c} = $c.",
            trap = "${c * c}", trapWhy = "${c * c} é |z|². O módulo pede a raiz.")
    },

    guide("probability", """P(A)=\frac{n_{A}}{n}""", "P(A) = n_A / n",
        "A probabilidade de A é o número de casos favoráveis dividido pelo total de casos igualmente prováveis.",
        listOf(sym("P(A)", "a probabilidade do evento A, entre 0 e 1"), sym("n_A", "a quantidade de resultados favoráveis"),
            sym("n", "a quantidade total de resultados igualmente prováveis")),
        listOf(
            step("""n=6""", "Liste o total: um dado tem 6 faces."),
            step("""n_{A}=3""", "Conte os favoráveis: as faces pares são 2, 4 e 6."),
            step("""P=\frac{3}{6}=\frac{1}{2}""", "Divida e simplifique."),
            step("""P(não\ A)=1-P(A)""", "O que não é A completa o total. As duas probabilidades somam 1.")
        ),
        listOf(
            real(Scene.Money, "Rifa", "Numa rifa de 100 números, quem compra 5 tem 5 chances em 100.",
                "P = 5/100 = 1/20",
                plug("Num sorteio com 200 rifas, você comprou 5. Qual a probabilidade de ganhar?", "1/40",
                    "Favoráveis: 5. Total: 200.", "5/200 = 1/40 = 2,5%.",
                    trap = "5/195", trapWhy = "O denominador é o total de rifas, incluindo as suas.")),
            real(Scene.Money, "Mega-Sena", "Existem 50 063 860 combinações de 6 números entre 60. Uma aposta simples tem 1 chance nesse total.",
                "P = 1/50 063 860"),
            real(Scene.Nature, "Previsão do tempo", "70% de chance de chuva significa 30% de chance de não chover.",
                "P(não chover) = 1 − 0,7 = 0,3",
                plug("A previsão dá 35% de chance de chuva. Qual a probabilidade de não chover? Responda em decimal.", "0.65",
                    "Use o complemento: 1 − P(chuva).", "1 − 0,35 = 0,65."))
        ),
        iso("Se P(chuva) = 0,3, quanto vale P(não chover)?", "0,7",
            "Os dois eventos cobrem todas as possibilidades, então somam 1.",
            "0,3" to "Esse é o próprio evento chuva, não o complemento.",
            "1,3" to "Uma probabilidade nunca passa de 1. Subtraia em vez de somar.")
    ) { a, b ->
        val total = a + b + 3
        plug("Uma urna tem $a bolas vermelhas e ${total - a} azuis. Qual a probabilidade de tirar vermelha?", "$a/$total",
            "Favoráveis: $a. Total: $a + ${total - a} = $total.", "P = $a/$total.",
            trap = "$a/${total - a}", trapWhy = "Divida pelo total de bolas, não só pelas azuis.")
    },

    guide("limits", """\lim_{x\to a}f(x)=L""", "lim (x → a) f(x) = L",
        "Quando x chega cada vez mais perto de a, f(x) chega cada vez mais perto de L.",
        listOf(sym("x → a", "x se aproximando de a, sem precisar chegar"), sym("f(x)", "a função analisada"),
            sym("L", "o valor do qual f(x) se aproxima")),
        listOf(
            step("""f(x)=\frac{x^{2}-4}{x-2}""", "Em x = 2 a conta dá 0/0, que não tem valor definido."),
            step("""\frac{(x-2)(x+2)}{x-2}""", "Fatore o numerador: x² − 4 = (x − 2)(x + 2)."),
            step("""x+2""", "Para x ≠ 2 o fator (x − 2) cancela."),
            step("""\lim_{x\to2}=4""", "Teste: x = 1,99 dá 3,99 e x = 2,01 dá 4,01. Os valores cercam 4.")
        ),
        listOf(
            real(Scene.Transport, "Velocímetro", "A velocidade instantânea é o limite de Δs/Δt quando o intervalo de tempo encolhe até quase zero.",
                "v = lim (Δt → 0) Δs/Δt"),
            real(Scene.Money, "Juros contínuos", "Dividir os juros em períodos cada vez menores faz (1 + 1/n)ⁿ se aproximar de e ≈ 2,718.",
                "lim (n → ∞) (1 + 1/n)ⁿ = e"),
            real(Scene.Lab, "Divisão por zero aparente", "Em física, muitas fórmulas dão 0/0 num ponto. O limite revela o valor real ali.",
                "(x² − 9)/(x − 3) → 6",
                plug("Calcule o limite de (x² − 9)/(x − 3) quando x tende a 3.", "6",
                    "Fatore: x² − 9 = (x − 3)(x + 3) e cancele.", "Sobra x + 3. Com x → 3, o limite é 6.",
                    trap = "0", trapWhy = "0/0 não é zero. Fatore e cancele antes de substituir."))
        ),
        iso("Se f(x) = 3x + 1, quanto é o limite quando x → 2?", "7",
            "Funções sem salto nem divisão por zero têm limite igual ao valor: 3 · 2 + 1.",
            "Não existe" to "A reta é contínua. O limite só falha com saltos ou divergências.",
            "3" to "3 é a inclinação. Substitua x = 2 na expressão.")
    ) { a, _ ->
        plug("Calcule o limite de (x² − ${a * a})/(x − $a) quando x → $a.", "${2 * a}",
            "Fatore: x² − ${a * a} = (x − $a)(x + $a).", "Cancele (x − $a) e use x = $a: $a + $a = ${2 * a}.",
            trap = "0", trapWhy = "Substituir direto dá 0/0, que não é zero. Fatore primeiro.")
    },

    guide("derivatives", """\frac{d}{dx}x^{n}=nx^{n-1}""", "d/dx (xⁿ) = n·xⁿ⁻¹",
        "Para derivar uma potência, desça o expoente multiplicando e diminua o expoente em 1.",
        listOf(sym("d/dx", "a derivada, a taxa de variação instantânea"), sym("x", "a variável"),
            sym("n", "o expoente da potência")),
        listOf(
            step("""f(x)=x^{3}""", "A derivada mede a inclinação da curva em cada ponto."),
            step("""f'(x)=3x^{2}""", "Desça o 3 para a frente e diminua o expoente para 2."),
            step("""f'(2)=3\cdot4=12""", "Em x = 2, a curva sobe 12 unidades de y por unidade de x."),
            step("""s(t)=5t^{2}\Rightarrow v(t)=10t""", "Na física: derivar a posição dá a velocidade.")
        ),
        listOf(
            real(Scene.Sport, "Queda livre", "Uma pedra cai com s(t) ≈ 4,9t². Derivando, v(t) = 9,8t. Depois de 2 s, ela cai a 19,6 m/s.",
                "v = ds/dt = 9,8t",
                plug("Uma bola cai com s(t) = 5t² metros. Qual a velocidade em t = 3 s?", "30",
                    "Derive: v(t) = 10t.", "Em t = 3: v = 30 m/s.", unit = "m/s",
                    trap = "45", trapWhy = "45 é a posição s(3). A velocidade é a derivada da posição.")),
            real(Scene.Transport, "Velocímetro", "O velocímetro mostra a derivada da posição: quantos metros o carro anda por segundo naquele instante.",
                "v = dx/dt"),
            real(Scene.Money, "Custo marginal", "Empresas usam a derivada do custo para saber quanto custa produzir uma unidade a mais.",
                "C'(q) = custo extra por unidade",
                plug("O custo de produzir q peças é C(q) = q² reais. Qual o custo marginal C'(q) em q = 50?", "100",
                    "Derive: C'(q) = 2q.", "Em q = 50: 2 × 50 = R\$ 100 por peça extra."))
        ),
        iso("Qual é a derivada de 4x³?", "12x²",
            "Desça o 3 multiplicando 4 e reduza o expoente: 4 · 3x².",
            "12x³" to "O expoente também diminui em 1.",
            "4x²" to "O expoente 3 precisa descer multiplicando o coeficiente.")
    ) { a, b ->
        val n = b % 3 + 2
        val derivative = a * n * (1 shl (n - 1))
        plug("Para f(x) = ${a}x^$n, calcule f′(2).", "$derivative",
            "Derive: f′(x) = ${a * n}x^${n - 1}.", "Substitua x = 2: ${a * n} × ${1 shl (n - 1)} = $derivative.",
            trap = "${a * (1 shl n)}", trapWhy = "Esse é f(2). Derive antes de substituir.")
    },

    guide("integrals", """\int_{a}^{b}f(x)\,dx""", "∫ de a até b de f(x) dx = área sob a curva",
        "A integral soma infinitas fatias finas de altura f(x) e largura dx entre a e b.",
        listOf(sym("∫", "a soma contínua das fatias"), sym("a, b", "os limites, onde a soma começa e termina"),
            sym("f(x)", "a altura de cada fatia"), sym("dx", "a largura de cada fatia, muito pequena")),
        listOf(
            step("""\int_{0}^{4}3\,dt""", "Uma função constante forma um retângulo."),
            step("""3\times4=12""", "Altura vezes largura: a área é 12."),
            step("""\int_{0}^{2}x\,dx=2""", "Uma reta forma um triângulo: base × altura ÷ 2 = 2 × 2 ÷ 2."),
            step("""\int_{0}^{T}v\,dt=distância""", "Na física, a área sob a velocidade é a distância percorrida.")
        ),
        listOf(
            real(Scene.Transport, "Distância percorrida", "Um carro a 60 km/h durante 2 h. A área sob o gráfico v × t é um retângulo.",
                "60 × 2 = 120 km",
                plug("Um carro anda a 20 m/s constantes por 15 s. Qual a distância percorrida?", "300",
                    "A área sob v(t) é um retângulo de altura 20 e largura 15.", "20 × 15 = 300 m.", unit = "m",
                    trap = "35", trapWhy = "35 é 20 + 15. A área multiplica altura e largura.")),
            real(Scene.Home, "Conta de luz", "Energia é a integral da potência no tempo. Um chuveiro de 5 kW ligado por 0,5 h consome 2,5 kWh.",
                "E = ∫P dt = 5 × 0,5 = 2,5 kWh",
                plug("Um aquecedor de 1,5 kW fica ligado por 4 h. Quantos kWh ele consome?", "6",
                    "A potência é constante: a área é um retângulo.", "1,5 × 4 = 6 kWh.")),
            real(Scene.Health, "Remédios", "Farmacologistas medem a área sob a curva de concentração no sangue (AUC) para saber quanto remédio o corpo recebeu.",
                "AUC = ∫C(t) dt")
        ),
        iso("O que ∫₀⁵ 4 dx representa?", "A área de um retângulo 5 × 4 = 20",
            "f(x) = 4 é constante entre 0 e 5. A área é altura vezes largura.",
            "4/5" to "Integrar soma áreas. Não há divisão aqui.",
            "4 + 5" to "Somar altura e largura não dá a área.")
    ) { a, b ->
        plug("Calcule a integral de f(x) = $a de x = 0 até x = $b.", "${a * b}",
            "A região é um retângulo de altura $a e largura $b.", "Área: $a × $b = ${a * b}.",
            trap = "${a + b}", trapWhy = "A área multiplica altura por largura.")
    },

    guide("linear-algebra", """v=c_{1}e_{1}+c_{2}e_{2}""", "v = c₁e₁ + c₂e₂",
        "Um vetor é uma receita: c₁ porções da direção e₁ mais c₂ porções da direção e₂.",
        listOf(sym("v", "o vetor descrito"), sym("e₁", "o primeiro vetor da base"), sym("e₂", "o segundo vetor da base"),
            sym("c₁", "a primeira coordenada, quanto de e₁ entra"), sym("c₂", "a segunda coordenada, quanto de e₂ entra")),
        listOf(
            step("""e_{1}=(1,0)\quad e_{2}=(0,1)""", "Escolha direções de referência."),
            step("""v=3e_{1}+2e_{2}""", "Três passos em e₁ e dois em e₂."),
            step("""v=(3,2)""", "As coordenadas são os coeficientes da combinação."),
            step("""2v=6e_{1}+4e_{2}""", "Multiplicar o vetor multiplica cada coordenada.")
        ),
        listOf(
            real(Scene.Computer, "Cores na tela", "Cada pixel é uma combinação de vermelho, verde e azul. Laranja ≈ 255·R + 165·G + 0·B.",
                "cor = r·R + g·G + b·B",
                plug("Um pixel tem cor (100, 40, 20). Se a imagem é clareada multiplicando o vetor por 2, qual o novo valor do verde?", "80",
                    "Multiplicar o vetor multiplica cada coordenada.", "40 × 2 = 80.")),
            real(Scene.Home, "Receita como vetor", "Um bolo usa (2 xícaras de farinha, 1 de açúcar, 3 ovos). Três bolos são 3 vezes esse vetor.",
                "3 · (2, 1, 3) = (6, 3, 9)",
                plug("Uma receita usa (3 ovos, 2 xícaras de leite). Quantos ovos para 4 receitas?", "12",
                    "Multiplique o vetor por 4.", "3 × 4 = 12 ovos.")),
            real(Scene.Lab, "Estados quânticos", "Um qubit |ψ⟩ = a|0⟩ + b|1⟩ é exatamente uma combinação linear numa base de dois vetores.",
                "|ψ⟩ = a|0⟩ + b|1⟩")
        ),
        iso("Se v = 5e₁ − 2e₂, quais são as coordenadas de v nessa base?", "(5, −2)",
            "As coordenadas são os coeficientes, na ordem dos vetores da base.",
            "(−2, 5)" to "A ordem segue a base: primeiro o coeficiente de e₁.",
            "(5, 2)" to "O sinal faz parte da coordenada.")
    ) { a, b ->
        plug("Some v = ${a}e₁ + ${b}e₂ e w = ${b}e₁ + 1e₂. Qual o coeficiente de e₁ no resultado?", "${a + b}",
            "Junte os termos em e₁: ${a}e₁ + ${b}e₁.", "Coeficiente: $a + $b = ${a + b}.",
            trap = "${a * b}", trapWhy = "Na soma de vetores, os coeficientes se somam.")
    },

    guide("matrices", """y_{i}=a_{i1}x_{1}+a_{i2}x_{2}""", "yᵢ = aᵢ₁x₁ + aᵢ₂x₂",
        "Cada linha da matriz multiplica a coluna de entrada termo a termo, e os produtos são somados.",
        listOf(sym("yᵢ", "a saída da linha i"), sym("aᵢⱼ", "o número da matriz na linha i e coluna j"),
            sym("xⱼ", "a entrada na posição j")),
        listOf(
            step("""A=(2\ \ 1)""", "Pegue uma linha da matriz."),
            step("""x=(1,\ 4)""", "Pegue a coluna de entrada."),
            step("""2\cdot1+1\cdot4""", "Multiplique par a par: primeiro com primeiro, segundo com segundo."),
            step("""=6""", "Some os produtos. Esse é o primeiro número da saída.")
        ),
        listOf(
            real(Scene.Money, "Total da compra", "Preços em linha × quantidades em coluna dão o total da compra numa única operação.",
                "(30, 20) · (3, 2) = 90 + 40 = 130",
                plug("Camisetas custam R\$ 30 e bonés R\$ 20. Você leva 3 camisetas e 2 bonés. Quanto paga?", "130",
                    "Linha (30, 20) vezes coluna (3, 2).", "30 × 3 + 20 × 2 = 90 + 40 = R\$ 130.",
                    trap = "90", trapWhy = "90 é só o primeiro produto. Some também 20 × 2.")),
            real(Scene.Phone, "Girar a foto", "Girar uma imagem 90° aplica a matriz ((0, −1), (1, 0)) a cada pixel: (x, y) vira (−y, x).",
                "(0·x − 1·y, 1·x + 0·y) = (−y, x)"),
            real(Scene.Computer, "Jogos 3D", "Placas de vídeo multiplicam bilhões de vetores por matrizes por segundo para girar, mover e projetar objetos.",
                "pixel = M · ponto")
        ),
        iso("Linha (2, 3) aplicada à coluna (4, 1) dá:", "2·4 + 3·1 = 11",
            "Multiplique par a par e some.",
            "(8, 3)" to "Os produtos precisam ser somados num único número.",
            "2·3 + 4·1 = 10" to "Os pares foram misturados. Primeiro da linha com primeiro da coluna.")
    ) { a, b ->
        val c = (a + b) % 5 + 1
        plug("Aplique a linha ($a, $b) à coluna ($c, 2).", "${a * c + b * 2}",
            "Multiplique par a par: $a × $c e $b × 2.", "Some ${a * c} + ${b * 2} = ${a * c + b * 2}.",
            trap = "${a * c}", trapWhy = "Faltou somar o segundo produto, $b × 2.")
    },

    guide("differential-equations", """y'=-ky\quad\Rightarrow\quad y(t)=y_{0}e^{-kt}""", "y′ = −ky ⇒ y(t) = y₀e⁻ᵏᵗ",
        "A quantidade diminui num ritmo proporcional ao que resta. O resultado é um decaimento exponencial.",
        listOf(sym("y", "a quantidade que muda com o tempo"), sym("y′", "a taxa de variação de y"),
            sym("k", "a constante de decaimento, por unidade de tempo"), sym("y₀", "o valor inicial"),
            sym("t", "o tempo")),
        listOf(
            step("""y'=-ky""", "Quanto mais há, mais rápido diminui."),
            step("""y'(0)=-ky_{0}""", "No início a queda é a mais rápida."),
            step("""y(t)=y_{0}e^{-kt}""", "A solução cai pela mesma fração a cada intervalo igual."),
            step("""t_{½}=\frac{\ln2}{k}""", "A meia-vida é o tempo para cair à metade. Depois de n meias-vidas, sobra y₀/2ⁿ.")
        ),
        listOf(
            real(Scene.Health, "Remédio no sangue", "Muitos remédios saem do corpo de forma exponencial. A cada meia-vida, a concentração cai pela metade.",
                "400 → 200 → 100 → 50 mg",
                plug("Um remédio tem meia-vida de 2 h. Você tomou 400 mg. Quantos mg restam após 6 h?", "50",
                    "6 h são 3 meias-vidas.", "400 ÷ 2³ = 400 ÷ 8 = 50 mg.",
                    trap = "100", trapWhy = "100 mg corresponde a 2 meias-vidas. Em 6 h há 3.")),
            real(Scene.Lab, "Carbono-14", "O carbono-14 tem meia-vida de cerca de 5 730 anos. Medir o que resta num fóssil indica sua idade.",
                "y = y₀/2^(t/5730)",
                plug("Um fóssil tem 1/4 do carbono-14 original. Quantos anos tem? Use meia-vida de 5730 anos.", "11460",
                    "1/4 = (1/2)², então passaram 2 meias-vidas.", "2 × 5730 = 11 460 anos.")),
            real(Scene.Home, "Café esfriando", "A diferença de temperatura entre o café e a sala também decai exponencialmente (lei de resfriamento de Newton).",
                "ΔT(t) = ΔT₀ e⁻ᵏᵗ")
        ),
        iso("Em y′ = −ky com k > 0, como y se comporta?", "Diminui cada vez mais devagar",
            "A taxa é proporcional a y. Conforme y diminui, a queda também diminui.",
            "Diminui em ritmo constante" to "Ritmo constante seria y′ = −k, sem o fator y.",
            "Cresce" to "O sinal negativo indica queda quando k > 0.")
    ) { a, b ->
        val halves = b % 3 + 1
        val start = a shl halves
        plug("Uma amostra tem $start g. Quanto resta depois de $halves meia(s)-vida(s)?", "$a",
            "Cada meia-vida divide por 2. Depois de $halves, divida por ${1 shl halves}.", "$start ÷ ${1 shl halves} = $a g.",
            unit = "g", trap = "${start - start / 2 * halves}", trapWhy = "O decaimento não tira a mesma quantidade a cada vez. Ele divide pela metade o que restou.")
    },

    guide("fourier", """s(t)=\sum A_{k}\cos(2\pi f_{k}t)""", "s(t) = Σ Aₖ cos(2π fₖ t)",
        "Todo sinal pode ser escrito como soma de ondas puras, cada uma com sua amplitude e frequência.",
        listOf(sym("s(t)", "o sinal completo ao longo do tempo"), sym("Aₖ", "a amplitude de cada onda pura"),
            sym("fₖ", "a frequência de cada onda pura", "Hz"), sym("t", "o tempo", "s")),
        listOf(
            step("""\cos(2\pi\cdot440t)""", "Uma onda pura: a nota lá, com 440 oscilações por segundo."),
            step("""s=\cos(2\pi f_{1}t)+\cos(2\pi f_{2}t)""", "Somar ondas forma um sinal mais complexo, como um acorde."),
            step("""espectro""", "A transformada de Fourier faz o caminho inverso: mostra um pico para cada frequência presente."),
            step("""altura\ do\ pico=A_{k}""", "A altura do pico mostra quanto daquela frequência existe no sinal.")
        ),
        listOf(
            real(Scene.Music, "Reconhecer música", "Aplicativos que identificam músicas comparam os picos do espectro do trecho gravado com um banco de dados.",
                "picos em fₖ = impressão digital",
                plug("Um acorde tem notas de 262 Hz, 330 Hz e 392 Hz. Quantos picos positivos o espectro ideal mostra?", "3",
                    "Cada nota pura gera um pico.", "Três frequências distintas: 3 picos.")),
            real(Scene.Computer, "MP3 e JPEG", "Compressores guardam só as frequências que você percebe e descartam o resto.",
                "arquivo ≈ poucas Aₖ importantes"),
            real(Scene.Health, "Ressonância magnética", "O aparelho mede sinais de rádio do corpo e usa a transformada de Fourier para montar a imagem.",
                "imagem = transformada do sinal"),
            real(Scene.Home, "Tomada", "A tensão da tomada é uma onda pura. Em 127 V, ela oscila como v(t) ≈ 179 cos(2π·60t).",
                "um único pico em 60 Hz",
                plug("Na Europa, a tensão da tomada é v(t) = 311cos(2π·50t). Qual a frequência, em Hz?", "50",
                    "Compare com A cos(2π f t).", "f = 50 Hz.", unit = "Hz",
                    trap = "311", trapWhy = "311 é a amplitude, em volts. A frequência multiplica 2πt."))
        ),
        iso("Qual a frequência de cos(2π·60t)?", "60 Hz",
            "Na forma cos(2π f t), f é o número que multiplica 2πt.",
            "2π·60 Hz" to "2π·60 é a frequência angular, em rad/s.",
            "1/60 Hz" to "1/60 s é o período, não a frequência.")
    ) { a, b ->
        plug("Em s(t) = ${a}cos(2π·${b * 100}t), qual é a frequência em Hz?", "${b * 100}",
            "Compare com A cos(2π f t).", "A frequência é ${b * 100} Hz.", unit = "Hz",
            trap = "$a", trapWhy = "$a é a amplitude. A frequência é o número que multiplica 2πt.")
    }
)
