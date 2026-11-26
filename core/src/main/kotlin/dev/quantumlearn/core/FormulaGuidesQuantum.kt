package dev.quantumlearn.core

internal fun quantumGuides(): List<FormulaGuide> = listOf(
    guide("blackbody", """\lambda_{p}=\frac{b}{T}""", "λₚ = b / T",
        "O comprimento de onda em que um corpo quente mais brilha é inversamente proporcional à temperatura (lei de Wien).",
        listOf(sym("λₚ", "o comprimento de onda do pico de emissão", "m"), sym("T", "a temperatura absoluta", "K"),
            sym("b", "a constante de Wien, cerca de 2,898 × 10⁻³", "m·K")),
        listOf(
            step("""\lambda_{p}T=b""", "O produto entre pico e temperatura é sempre o mesmo."),
            step("""\lambda_{p}=\frac{2{,}898\times10^{-3}}{5800}""", "O Sol tem superfície de cerca de 5 800 K."),
            step("""\lambda_{p}\approx5\times10^{-7}\,m=500\,nm""", "O pico do Sol fica no visível, perto do verde-azulado."),
            step("""T\uparrow\Rightarrow\lambda_{p}\downarrow""", "Mais quente, mais azul. Mais frio, mais vermelho e infravermelho.")
        ),
        listOf(
            real(Scene.Health, "Câmera térmica", "O corpo humano, a uns 310 K, brilha com pico perto de 9,3 µm, no infravermelho. Câmeras térmicas enxergam essa luz.",
                "λₚ = 2,898 × 10⁻³ ÷ 310 ≈ 9,3 µm",
                plug("Qual o pico de emissão do corpo humano a 310 K, em µm? Use b = 2898 µm·K.", "2898/310",
                    "Use λₚ = b/T = 2898/310.", "λₚ ≈ 9,35 µm, no infravermelho.", unit = "µm",
                    trap = "898380", trapWhy = "λₚ e T são inversos: divida b pela temperatura.", tolerance = 2e-3)),
            real(Scene.Space, "Cor das estrelas", "Estrelas azuladas passam de 10 000 K. Estrelas avermelhadas ficam perto de 3 000 K.",
                "10 000 K ⇒ λₚ ≈ 290 nm",
                plug("Uma estrela tem T = 10 000 K. Qual o pico em nm? Use b = 2,898e-3 m·K.", "289.8",
                    "λₚ = 2,898e-3 ÷ 10 000 = 2,898e-7 m.", "Isso é 289,8 nm, no ultravioleta.", unit = "nm", tolerance = 2e-3)),
            real(Scene.Home, "Lâmpada incandescente", "O filamento a 2 800 K tem pico no infravermelho, perto de 1 µm. Por isso a maior parte da energia vira calor.",
                "λₚ ≈ 1 µm")
        ),
        iso("Isolando T em λₚT = b:", "T = b / λₚ",
            "Divida os dois lados por λₚ.",
            "T = b · λₚ" to "Multiplicar de novo por λₚ não isola T.",
            "T = λₚ / b" to "A razão ficou invertida.")
    ) { a, _ ->
        plug("Um objeto está a ${a * 1000} K. Qual o pico de emissão em nm? Use b = 2,898e-3 m·K.", "2898000/${a * 1000}",
            "λₚ = 2,898e-3 ÷ ${a * 1000} m.", "Convertendo para nm: ${br(2898000.0 / (a * 1000))} nm.", unit = "nm",
            trap = "${2898 * a}", trapWhy = "Temperatura maior dá pico menor: divida b por T.", tolerance = 1e-4)
    },

    guide("photons", """E=hf=\frac{hc}{\lambda}""", "E = h · f = h · c / λ",
        "Cada fóton carrega energia igual à constante de Planck vezes sua frequência.",
        listOf(sym("E", "a energia de um fóton", "J ou eV"), sym("h", "a constante de Planck, 6,626 × 10⁻³⁴", "J·s"),
            sym("f", "a frequência da luz", "Hz"), sym("λ", "o comprimento de onda", "m")),
        listOf(
            step("""E=hf""", "A energia vem em pacotes. Frequência maior, pacote maior."),
            step("""E=\frac{hc}{\lambda}""", "Como c = λf, também dá para usar o comprimento de onda."),
            step("""hc\approx1240\,eV\cdot nm""", "Atalho prático: E em eV = 1240 ÷ λ em nm."),
            step("""E_{650\,nm}=\frac{1240}{650}\approx1{,}9\,eV""", "Um laser vermelho tem fótons de cerca de 1,9 eV.")
        ),
        listOf(
            real(Scene.Health, "Protetor solar", "Fótons UV-B de 300 nm têm cerca de 4,1 eV, energia suficiente para danificar o DNA da pele. Luz visível não tem.",
                "E = 1240 ÷ 300 ≈ 4,1 eV",
                plug("Qual a energia de um fóton UV-B de 310 nm, em eV? Use E = 1240/λ(nm).", "4",
                    "Substitua λ = 310 nm.", "1240 ÷ 310 = 4 eV.", unit = "eV",
                    trap = "0.25", trapWhy = "Use 1240 dividido pelo comprimento de onda.")),
            real(Scene.Phone, "Wi-Fi e luz", "Um fóton de Wi-Fi de 2,4 GHz tem cerca de 10⁻⁵ eV, centenas de milhares de vezes menos que um fóton de luz visível.",
                "E = h × 2,4 × 10⁹ ≈ 1 × 10⁻⁵ eV"),
            real(Scene.Light, "LEDs coloridos", "A cor de um LED depende da energia liberada por elétron no material. LEDs azuis precisam de cerca de 2,7 eV.",
                "λ = 1240 ÷ 2,7 ≈ 460 nm",
                plug("Um LED emite fótons de 2,5 eV. Qual o comprimento de onda em nm? Use λ = 1240/E.", "496",
                    "Isole λ = 1240/E.", "1240 ÷ 2,5 = 496 nm, luz azul-esverdeada.", unit = "nm"))
        ),
        iso("Isolando f em E = hf:", "f = E/h",
            "Divida os dois lados por h.",
            "f = E · h" to "Multiplicar por h não isola f.",
            "f = h/E" to "A razão ficou invertida.")
    ) { a, _ ->
        plug("Calcule a energia de um fóton de ${a * 100} nm, em eV. Use E = 1240/λ(nm).", "1240/${a * 100}",
            "Substitua λ = ${a * 100} nm.", "E = 1240 ÷ ${a * 100} ≈ ${br(1240.0 / (a * 100))} eV.", unit = "eV",
            trap = "${a * 100}/1240", trapWhy = "A energia é 1240 dividido por λ.", tolerance = 1e-4)
    },

    guide("photoelectric", """K_{max}=hf-\phi""", "K_max = h·f − φ",
        "A energia do fóton paga a saída do elétron (φ). O que sobra vira energia cinética.",
        listOf(sym("K_max", "a energia cinética máxima do elétron ejetado", "eV"), sym("hf", "a energia do fóton", "eV"),
            sym("φ", "a função trabalho, o custo para arrancar um elétron", "eV")),
        listOf(
            step("""hf=6\,eV""", "Um fóton chega com 6 eV."),
            step("""\phi=4{,}3\,eV""", "O zinco cobra 4,3 eV para soltar um elétron."),
            step("""K_{max}=6-4{,}3=1{,}7\,eV""", "Sobra 1,7 eV de movimento."),
            step("""hf<\phi\Rightarrow sem\ elétrons""", "Abaixo do limiar nada sai, por mais intensa que seja a luz.")
        ),
        listOf(
            real(Scene.Phone, "Câmera do celular", "Cada pixel do sensor converte fótons em elétrons. Contar elétrons é medir o brilho daquele ponto.",
                "fóton → elétron → sinal"),
            real(Scene.Light, "Painel solar", "Fótons com energia acima do limiar do silício (cerca de 1,1 eV) liberam elétrons que viram corrente.",
                "hf > 1,1 eV ⇒ corrente",
                plug("Um fóton de 2 eV atinge uma célula de silício com limiar de 1,1 eV. Quanta energia sobra para o elétron?", "0.9",
                    "Use K = hf − φ = 2 − 1,1.", "K = 0,9 eV.", unit = "eV",
                    trap = "3.1", trapWhy = "A função trabalho é subtraída, porque é um custo.")),
            real(Scene.Home, "Portas automáticas", "Fotocélulas detectam se um feixe de luz foi interrompido e acionam portas, alarmes e iluminação pública.",
                "luz bloqueada ⇒ corrente cai")
        ),
        iso("Se hf < φ, o que acontece?", "Nenhum elétron é ejetado",
            "O fóton não paga o custo de saída. Aumentar a intensidade só traz mais fótons fracos.",
            "Elétrons saem com K negativa" to "Energia cinética não pode ser negativa. O elétron simplesmente não sai.",
            "Elétrons saem devagar" to "Abaixo do limiar não sai nenhum elétron.")
    ) { a, b ->
        plug("Um fóton de ${a + b} eV atinge um metal com φ = $b eV. Qual o K máximo?", "$a",
            "Use K = hf − φ = ${a + b} − $b.", "K = $a eV.", unit = "eV",
            trap = "${a + 2 * b}", trapWhy = "Subtraia a função trabalho em vez de somar.")
    },

    guide("duality", """\lambda=\frac{h}{p}=\frac{h}{mv}""", "λ = h / p = h / (m·v)",
        "Toda partícula com momento p se comporta como onda de comprimento h/p (de Broglie).",
        listOf(sym("λ", "o comprimento de onda de de Broglie", "m"), sym("h", "a constante de Planck", "J·s"),
            sym("p", "o momento da partícula", "kg·m/s"), sym("m", "a massa", "kg"), sym("v", "a velocidade", "m/s")),
        listOf(
            step("""p=mv""", "Calcule o momento."),
            step("""\lambda=\frac{6{,}63\times10^{-34}}{p}""", "Divida a constante de Planck pelo momento."),
            step("""elétron:\ \lambda\sim10^{-10}\,m""", "Para elétrons, λ é do tamanho de um átomo e a onda aparece."),
            step("""bola:\ \lambda\sim10^{-35}\,m""", "Para objetos do dia a dia, λ é pequeno demais para notar.")
        ),
        listOf(
            real(Scene.Lab, "Microscópio eletrônico", "Elétrons acelerados têm λ milhares de vezes menor que a luz visível, por isso mostram vírus e até átomos.",
                "λ_elétron ≪ λ_luz",
                plug("Um elétron (m = 9,1e-31 kg) anda a 1e6 m/s. Qual seu λ em nm? Use h = 6,63e-34 J·s.", "0.7286",
                    "p = 9,1e-31 × 1e6 = 9,1e-25 kg·m/s.", "λ = 6,63e-34 ÷ 9,1e-25 ≈ 7,29e-10 m ≈ 0,73 nm.", unit = "nm",
                    tolerance = 2e-3)),
            real(Scene.Sport, "Bola de tênis", "Uma bola de 0,06 kg a 50 m/s tem λ ≈ 2 × 10⁻³⁴ m, muito menor que um próton. Nenhuma interferência é perceptível.",
                "λ = 6,63e-34 ÷ (0,06 × 50) ≈ 2,2e-34 m"),
            real(Scene.Computer, "Difração de elétrons", "Feixes de elétrons difratam em cristais. A técnica revela a estrutura de materiais usados em chips.",
                "λ ≈ distância entre átomos")
        ),
        iso("Se o momento dobra, o comprimento de onda:", "Cai à metade",
            "p está no denominador de λ = h/p.",
            "Dobra" to "λ e p são inversamente proporcionais.",
            "Não muda" to "λ depende de p.")
    ) { a, _ ->
        plug("Uma partícula tem p = $a × 10⁻²⁴ kg·m/s. Qual o λ, em m? Use h ≈ 6,626e-34.", "h/(${a}e-24)",
            "Use λ = h/p = 6,626e-34 ÷ ${a}e-24.", "λ ≈ ${br(6.62607015 / a)} × 10⁻¹⁰ m.", unit = "m",
            trap = "${a}e-24/h", trapWhy = "λ é h dividido pelo momento.", tolerance = 5e-3)
    },

    guide("double-slit", """P\propto|a+b|^{2}\quad\Delta y=\frac{\lambda L}{d}""", "P ∝ |a + b|² · Δy = λL / d",
        "Some as amplitudes das duas fendas antes de elevar ao quadrado. As franjas ficam separadas por λL/d.",
        listOf(sym("a, b", "as amplitudes vindas de cada fenda"), sym("P", "a probabilidade de detectar naquele ponto"),
            sym("Δy", "a distância entre franjas claras", "m"), sym("λ", "o comprimento de onda", "m"),
            sym("L", "a distância das fendas até a tela", "m"), sym("d", "a separação entre as fendas", "m")),
        listOf(
            step("""|1+1|^{2}=4""", "Em fase, as amplitudes se reforçam: franja clara."),
            step("""|1-1|^{2}=0""", "Em oposição, elas se cancelam: franja escura."),
            step("""|1|^{2}+|1|^{2}=2""", "Somar probabilidades daria 2 em todo lugar e as franjas sumiriam. A natureza soma amplitudes."),
            step("""\Delta y=\frac{\lambda L}{d}""", "λ maior ou fendas mais juntas deixam as franjas mais espaçadas.")
        ),
        listOf(
            real(Scene.Home, "Arco-íris do CD", "As trilhas de um CD funcionam como milhares de fendas. Cada cor interfere em um ângulo diferente.",
                "d ≈ 1,6 µm entre trilhas"),
            real(Scene.Nature, "Bolhas de sabão", "Luz refletida nas duas faces da película interfere. A espessura escolhe qual cor se reforça.",
                "caminhos diferentes ⇒ cores"),
            real(Scene.Lab, "Laser na parede", "Um laser vermelho passando por duas fendas forma franjas visíveis numa parede distante.",
                "Δy = 650 nm × 2 m ÷ 0,1 mm = 13 mm",
                plug("Laser de 650 nm, fendas separadas por 0,1 mm, tela a 2 m. Qual a distância entre franjas, em mm?", "13",
                    "Use Δy = λL/d = 650e-9 × 2 ÷ 1e-4.", "Δy = 0,013 m = 13 mm.", unit = "mm",
                    trap = "0.0013", trapWhy = "Confira as potências de 10: 650e-9 × 2 ÷ 1e-4 = 1,3e-2 m.", tolerance = 1e-4))
        ),
        iso("Duas amplitudes iguais em oposição de fase (a = 1, b = −1) dão P proporcional a:", "0",
            "|1 + (−1)|² = 0: franja escura.",
            "2" to "2 seria a soma das probabilidades, que ignora a interferência.",
            "4" to "4 acontece quando as amplitudes estão em fase.")
    ) { a, b ->
        plug("Laser de ${a * 100} nm, fendas a 0,1 mm, tela a $b m. Qual Δy em mm?", "${a * b}",
            "Use Δy = λL/d = ${a * 100}e-9 × $b ÷ 1e-4.", "Δy = ${a * b}e-3 m = ${a * b} mm.", unit = "mm",
            trap = "${a * b * 100}", trapWhy = "Converta nm para m: ${a * 100} nm = ${a * 100}e-9 m.")
    },

    guide("uncertainty", """\Delta x\,\Delta p\ge\frac{\hbar}{2}""", "Δx · Δp ≥ ℏ / 2",
        "Quanto mais localizada a partícula, mais espalhado fica o seu momento. O produto das incertezas tem um mínimo.",
        listOf(sym("Δx", "a incerteza na posição", "m"), sym("Δp", "a incerteza no momento", "kg·m/s"),
            sym("ℏ", "a constante de Planck reduzida, 1,055 × 10⁻³⁴", "J·s")),
        listOf(
            step("""\Delta x\downarrow""", "Prender a partícula num espaço menor..."),
            step("""\Delta p\ge\frac{\hbar}{2\Delta x}""", "...exige uma mistura maior de momentos."),
            step("""\Delta x=10^{-10}\Rightarrow\Delta p\ge5\times10^{-25}""", "Um elétron do tamanho de um átomo."),
            step("""não\ é\ erro\ do\ aparelho""", "É uma propriedade das ondas: um pacote curto precisa de muitas frequências.")
        ),
        listOf(
            real(Scene.Lab, "Tamanho dos átomos", "Se o elétron caísse no núcleo, Δx seria minúsculo e Δp enorme, com energia cinética alta demais. Isso fixa o tamanho dos átomos.",
                "Δp ≥ ℏ/(2Δx)",
                plug("Um elétron está confinado em Δx = 1e-10 m. Qual o Δp mínimo, em kg·m/s? Use ℏ = 1,05e-34.", "5.25e-25",
                    "Use Δp = ℏ/(2Δx) = 1,05e-34 ÷ 2e-10.", "Δp ≈ 5,25e-25 kg·m/s.",
                    trap = "1.05e-44", trapWhy = "Divida ℏ por 2Δx em vez de multiplicar.", tolerance = 1e-3)),
            real(Scene.Light, "Pulsos de laser", "Lasers de femtossegundos têm pulsos tão curtos que seu espectro precisa ser largo. Pulso curto, cores espalhadas.",
                "Δt · Δf ≳ 1/(4π)"),
            real(Scene.Sport, "Bola de futebol", "Para uma bola, ℏ/2 é tão pequeno que a incerteza fica muito abaixo de qualquer medida possível.",
                "Δx ~ 10⁻³⁴ m: irrelevante")
        ),
        iso("Isolando o Δp mínimo:", "Δp = ℏ / (2Δx)",
            "Divida os dois lados por Δx.",
            "Δp = 2Δx / ℏ" to "A razão ficou invertida.",
            "Δp = ℏ · Δx / 2" to "Δx precisa dividir. Mais localização exige mais incerteza no momento.")
    ) { a, _ ->
        plug("Uma partícula tem Δx = $a × 10⁻¹⁰ m. Qual o Δp mínimo? Use ℏ ≈ 1,055e-34.", "hbar/(2*${a}e-10)",
            "Use Δp = ℏ/(2Δx) = 1,055e-34 ÷ (2 × ${a}e-10).", "Δp ≈ ${br(1.0545718 / (2 * a))} × 10⁻²⁴ kg·m/s.",
            trap = "hbar*${a}e-10/2", trapWhy = "Δx divide ℏ. Quanto menor Δx, maior Δp.", tolerance = 5e-3)
    },

    guide("wave-function", """\rho(x)=|\psi(x)|^{2}""", "ρ(x) = |ψ(x)|²",
        "A densidade de probabilidade num ponto é o quadrado do módulo da função de onda ali.",
        listOf(sym("ψ(x)", "a função de onda, uma amplitude em cada ponto"), sym("ρ(x)", "a densidade de probabilidade"),
            sym("x", "a posição")),
        listOf(
            step("""\psi(x)=0{,}5""", "A amplitude pode ser positiva, negativa ou complexa."),
            step("""\rho=|0{,}5|^{2}=0{,}25""", "O quadrado do módulo é sempre positivo."),
            step("""P\approx\rho\cdot\Delta x""", "Num intervalo pequeno, a probabilidade é densidade vezes largura."),
            step("""\int\rho\,dx=1""", "Somando todo o espaço, a partícula está em algum lugar com certeza.")
        ),
        listOf(
            real(Scene.Lab, "Microscópio de tunelamento", "Imagens de átomos numa superfície mostram, na prática, onde |ψ|² dos elétrons é maior.",
                "brilho ∝ |ψ|²"),
            real(Scene.Light, "Orbitais e densidades", "As 'nuvens' dos livros de química são desenhos de |ψ|². Elas mostram onde o elétron tende a ser encontrado.",
                "nuvem = ρ(x)",
                plug("Num ponto, ψ = 0,3. Num intervalo de largura 0,1, qual a probabilidade aproximada? Use P ≈ |ψ|²·Δx.", "0.009",
                    "Calcule ρ = 0,3² = 0,09.", "P ≈ 0,09 × 0,1 = 0,009.",
                    trap = "0.03", trapWhy = "Eleve ψ ao quadrado antes de multiplicar pela largura.")),
            real(Scene.Computer, "Química no computador", "Programas calculam ψ de moléculas para prever reações e testar remédios antes de ir ao laboratório.",
                "ψ ⇒ |ψ|² ⇒ propriedades")
        ),
        iso("ψ = −0,5 e ψ = +0,5 em dois pontos dão densidades:", "Iguais, 0,25 nos dois",
            "O módulo ao quadrado apaga o sinal.",
            "Opostas" to "Densidade de probabilidade nunca é negativa.",
            "−0,25 e 0,25" to "|−0,5|² = 0,25, não −0,25.")
    ) { a, _ ->
        plug("A amplitude num ponto vale ψ = −$a/10. Qual |ψ|²?", "${a * a}/100",
            "Pegue o módulo: $a/10.", "Eleve ao quadrado: ${a * a}/100 = ${br(a * a / 100.0)}.",
            trap = "-$a/10", trapWhy = "Falta elevar ao quadrado o módulo da amplitude.")
    },

    guide("born", """P_{j}=|a_{j}|^{2}""", "Pⱼ = |aⱼ|²",
        "A probabilidade de medir o resultado j é o quadrado do módulo da amplitude desse resultado.",
        listOf(sym("Pⱼ", "a probabilidade do resultado j"), sym("aⱼ", "a amplitude associada ao resultado j"),
            sym("j", "o rótulo de cada resultado possível")),
        listOf(
            step("""|\psi\rangle=0{,}6|0\rangle+0{,}8|1\rangle""", "Cada resultado tem uma amplitude."),
            step("""P_{0}=0{,}6^{2}=0{,}36""", "36% de chance de medir 0."),
            step("""P_{1}=0{,}8^{2}=0{,}64""", "64% de chance de medir 1."),
            step("""0{,}36+0{,}64=1""", "As probabilidades somam 1.")
        ),
        listOf(
            real(Scene.Computer, "Computador quântico", "Rodar o mesmo circuito mil vezes e contar os resultados é como se mede |a|² na prática.",
                "contagens ≈ 1000 × |a|²",
                plug("Um qubit tem amplitude 0,6 para |0⟩. Em 1000 medições, quantas vezes se espera obter 0?", "360",
                    "P(0) = 0,6² = 0,36.", "1000 × 0,36 = 360.",
                    trap = "600", trapWhy = "A probabilidade é a amplitude ao quadrado, não a amplitude.")),
            real(Scene.Light, "Óculos polarizados", "Luz polarizada a 45° passa por um polarizador vertical com probabilidade cos² 45° = 1/2, fóton a fóton.",
                "P = cos² θ"),
            real(Scene.Lab, "Detectores de fótons", "Detectores contam fótons um a um. As contagens reproduzem as probabilidades previstas por |a|².",
                "frequência ≈ |a|²")
        ),
        iso("Com amplitude a = i/√2, quanto vale P?", "1/2",
            "|i/√2|² = |i|²/2 = 1/2.",
            "i/2" to "Probabilidade é um número real. Use o módulo.",
            "−1/2" to "(i/√2)² = −1/2, mas a regra usa o módulo ao quadrado, sempre positivo.")
    ) { a, b ->
        val (x, y, c) = triples[(a + b) % triples.size]
        plug("Um estado tem amplitudes $x/$c e $y/$c. Qual a probabilidade do primeiro resultado?", "${x * x}/${c * c}",
            "Eleve a amplitude ao quadrado.", "($x/$c)² = ${x * x}/${c * c}.",
            trap = "$x/$c", trapWhy = "Essa é a amplitude. A probabilidade é o seu quadrado.")
    },

    guide("superposition", """|s\rangle=a|u\rangle+b|v\rangle""", "|s⟩ = a|u⟩ + b|v⟩",
        "Um estado pode ser a soma de outros estados, cada um com sua amplitude. A medida escolhe um deles com probabilidade |amplitude|².",
        listOf(sym("|s⟩", "o estado em superposição"), sym("|u⟩, |v⟩", "os estados combinados"),
            sym("a", "a amplitude de |u⟩"), sym("b", "a amplitude de |v⟩")),
        listOf(
            step("""\frac{|0\rangle+|1\rangle}{\sqrt{2}}""", "Duas amplitudes iguais."),
            step("""P=\left(\frac{1}{\sqrt{2}}\right)^{2}=\frac{1}{2}""", "Cada resultado aparece em metade das medidas."),
            step("""n\ estados\ iguais\Rightarrow P=\frac{1}{n}""", "Com n amplitudes iguais, cada uma tem chance 1/n."),
            step("""a,b\ interferem""", "Diferente de uma moeda escondida, as amplitudes podem se somar ou se cancelar depois.")
        ),
        listOf(
            real(Scene.Computer, "Porta Hadamard", "Todo algoritmo quântico começa criando superposições com a porta H, que leva |0⟩ em (|0⟩ + |1⟩)/√2.",
                "H|0⟩ = (|0⟩ + |1⟩)/√2",
                plug("O estado é (|0⟩ + |1⟩)/√2. Em 500 medições, quantas vezes se espera o resultado 1?", "250",
                    "P(1) = (1/√2)² = 1/2.", "500 × 1/2 = 250.",
                    trap = "353.5534", trapWhy = "Use a probabilidade 1/2, não a amplitude 1/√2.", tolerance = 1e-3)),
            real(Scene.Light, "Polarização diagonal", "Luz polarizada a 45° é superposição de horizontal e vertical. Um filtro vertical deixa passar metade.",
                "|D⟩ = (|H⟩ + |V⟩)/√2"),
            real(Scene.Lab, "Interferômetros", "Num interferômetro, cada fóton segue dois caminhos em superposição e interfere consigo mesmo.",
                "dois caminhos ⇒ franjas")
        ),
        iso("O que caracteriza uma superposição?", "Amplitudes somadas que podem interferir",
            "As amplitudes coexistem e podem se reforçar ou se cancelar antes da medida.",
            "O sistema já escolheu um resultado e só não sabemos qual" to "Essa seria uma mistura clássica, que não produz interferência.",
            "As probabilidades podem ser negativas" to "Amplitudes podem ser negativas. Probabilidades, não.")
    ) { a, _ ->
        plug("Um estado normalizado tem $a amplitudes iguais. Qual a probabilidade de cada resultado?", "1/$a",
            "Cada amplitude vale 1/√$a.", "P = (1/√$a)² = 1/$a.",
            trap = "1/sqrt($a)", trapWhy = "1/√$a é a amplitude. A probabilidade é o quadrado.")
    },

    guide("quantum-states", """|a|^{2}+|b|^{2}=1""", "|a|² + |b|² = 1",
        "Um estado físico é normalizado: as probabilidades de todos os resultados somam 1.",
        listOf(sym("a", "a amplitude do primeiro resultado"), sym("b", "a amplitude do segundo resultado"),
            sym("1", "a probabilidade total, certeza de obter algum resultado")),
        listOf(
            step("""(3,4)""", "Um vetor qualquer ainda não é um estado físico."),
            step("""\sqrt{3^{2}+4^{2}}=5""", "Calcule a norma."),
            step("""\left(\frac{3}{5},\frac{4}{5}\right)""", "Divida cada componente pela norma."),
            step("""\frac{9}{25}+\frac{16}{25}=1""", "Agora as probabilidades somam 1.")
        ),
        listOf(
            real(Scene.Computer, "Simuladores quânticos", "Bibliotecas como Qiskit recusam estados cujas probabilidades não somam 1.",
                "|a|² + |b|² = 1",
                plug("Um qubit tem a = 0,6. Quanto vale |b|²?", "0.64",
                    "Use |b|² = 1 − |a|² = 1 − 0,36.", "|b|² = 0,64.",
                    trap = "0.4", trapWhy = "Subtraia |a|² = 0,36, não a amplitude 0,6.")),
            real(Scene.Lab, "Calibração", "Experimentos checam a normalização: a soma das frequências de todos os resultados deve dar 100%.",
                "Σ Pⱼ = 1")
        ),
        iso("Como normalizar o vetor (1, 1)?", "Dividir por √2",
            "A norma é √(1² + 1²) = √2.",
            "Dividir por 2" to "(1/2)² + (1/2)² = 1/2, ainda não normalizado.",
            "Multiplicar por √2" to "Isso aumenta a norma para 2.")
    ) { a, b ->
        val (x, y, c) = triples[(a * 3 + b) % triples.size]
        plug("Normalize ($x, $y). Qual é a primeira componente?", "$x/$c",
            "A norma é √($x² + $y²) = $c.", "Primeira componente: $x/$c.",
            trap = "$x/${x + y}", trapWhy = "Divida pela norma √(x² + y²), não pela soma das componentes.")
    },

    guide("operators", """X|0\rangle=|1\rangle\quad X|1\rangle=|0\rangle""", "X|0⟩ = |1⟩ · X|1⟩ = |0⟩",
        "Um operador transforma um estado em outro. A porta X troca |0⟩ e |1⟩, como um NOT.",
        listOf(sym("X", "o operador NOT quântico"), sym("|0⟩", "o estado base zero"), sym("|1⟩", "o estado base um")),
        listOf(
            step("""X|0\rangle=|1\rangle""", "X vira o bit."),
            step("""X(a|0\rangle+b|1\rangle)=b|0\rangle+a|1\rangle""", "Em superposição, X troca as amplitudes."),
            step("""XX=I""", "Aplicar duas vezes desfaz: volta ao início."),
            step("""Z|1\rangle=-|1\rangle""", "Outros operadores mudam fases em vez de trocar estados.")
        ),
        listOf(
            real(Scene.Computer, "Porta NOT", "Toda plataforma de computação quântica oferece a porta X, equivalente ao NOT clássico.",
                "X: 0 ↔ 1",
                plug("Comece em |0⟩ e aplique X sete vezes. Termina em |0⟩ ou |1⟩? Responda 0 ou 1.", "1",
                    "Cada par de aplicações se desfaz.", "Sete é ímpar: sobra uma troca, resultado 1.",
                    trap = "0", trapWhy = "Com número ímpar de aplicações, sobra uma troca.")),
            real(Scene.Light, "Placa de meia onda", "Uma lâmina óptica pode trocar a polarização horizontal pela vertical, agindo como X num fóton.",
                "|H⟩ ↔ |V⟩"),
            real(Scene.Lab, "Medir energia", "Cada grandeza física tem um operador. Medir energia é perguntar qual autovalor do operador H aparece.",
                "grandeza ↔ operador")
        ),
        iso("Quanto vale X aplicado a a|0⟩ + b|1⟩?", "b|0⟩ + a|1⟩",
            "X troca os rótulos, então as amplitudes trocam de lugar.",
            "a|0⟩ + b|1⟩" to "Esse é o estado original. X muda o estado.",
            "−a|0⟩ − b|1⟩" to "X não muda sinais, ele troca os estados.")
    ) { a, b ->
        val n = a + b
        plug("Comece em |0⟩ e aplique X $n vezes. Qual a primeira componente do vetor final?", "${(n + 1) % 2}",
            "Cada par de aplicações volta ao início.", if (n % 2 == 0) "$n é par: o vetor volta a (1, 0)." else "$n é ímpar: o vetor termina em (0, 1).",
            trap = "${n % 2}", trapWhy = "Conte a paridade: número par de trocas volta ao estado inicial.")
    },

    guide("eigenvalues", """Av=\lambda v""", "A·v = λ·v",
        "Um autovetor é uma direção que o operador só estica ou encolhe. O fator é o autovalor.",
        listOf(sym("A", "o operador ou matriz"), sym("v", "o autovetor, diferente de zero"), sym("λ", "o autovalor, o fator de escala")),
        listOf(
            step("""A=(2\ \ 0;\ \ 0\ \ 5)""", "Uma matriz diagonal age separadamente em cada eixo."),
            step("""A(0,1)=(0,5)""", "O vetor (0, 1) sai esticado 5 vezes, na mesma direção."),
            step("""\lambda=5""", "Esse fator é o autovalor."),
            step("""H\varphi=E\varphi""", "Em quântica, as energias permitidas são autovalores do operador H.")
        ),
        listOf(
            real(Scene.Light, "Luz do neon", "Cada cor emitida corresponde à diferença entre dois autovalores de energia do átomo.",
                "ΔE = Eₙ − Eₘ"),
            real(Scene.Computer, "Busca do Google", "O PageRank original ordenava páginas pelo autovetor de uma matriz de links.",
                "A·v = 1·v"),
            real(Scene.Home, "Vibração de pontes", "As frequências naturais de uma estrutura vêm de autovalores. Engenheiros evitam que ventos e passos coincidam com elas.",
                "frequências naturais = autovalores",
                plug("Um modelo de ponte tem matriz A = (2 0; 0 5) e modo de vibração v = (0, 1). Qual o autovalor desse modo?", "5",
                    "Calcule Av = (0, 5).", "Av = 5v, então λ = 5.",
                    trap = "2", trapWhy = "2 age no outro eixo. Para (0, 1), o fator é 5."))
        ),
        iso("Se Av = 3v com v ≠ 0, então:", "v é autovetor com autovalor 3",
            "O operador apenas multiplicou v por 3.",
            "3v é o autovalor" to "O autovalor é o número 3. O vetor é v.",
            "v não é autovetor" to "Se A só reescala v, então v é autovetor.")
    ) { a, b ->
        plug("A = ($a $b; $b $a) e v = (1, 1). Calcule λ em Av = λv.", "${a + b}",
            "Av = ($a + $b, $b + $a) = (${a + b}, ${a + b}).", "Isso é ${a + b} × (1, 1), então λ = ${a + b}.",
            trap = "${a * b}", trapWhy = "Multiplique a matriz pelo vetor: cada linha soma $a + $b.")
    },

    guide("dirac", """\langle0|\psi\rangle=a""", "⟨0|ψ⟩ = a",
        "O bra ⟨0| aplicado ao ket |ψ⟩ extrai a amplitude de |ψ⟩ na direção de |0⟩.",
        listOf(sym("|ψ⟩", "o ket, o estado"), sym("⟨0|", "o bra, que projeta na direção de |0⟩"),
            sym("a", "a amplitude obtida, um número")),
        listOf(
            step("""|\psi\rangle=a|0\rangle+b|1\rangle""", "Escreva o estado na base."),
            step("""\langle0|0\rangle=1\quad\langle0|1\rangle=0""", "Os vetores da base são ortonormais."),
            step("""\langle0|\psi\rangle=a""", "O bra seleciona só a parte em |0⟩."),
            step("""\langle\psi|\psi\rangle=|a|^{2}+|b|^{2}""", "O produto do estado consigo mesmo dá a norma ao quadrado.")
        ),
        listOf(
            real(Scene.Computer, "Documentação quântica", "Livros e bibliotecas de computação quântica escrevem portas e estados em notação de Dirac.",
                "|ψ⟩, ⟨φ|ψ⟩",
                plug("|ψ⟩ = 0,8|0⟩ + 0,6|1⟩. Quanto vale ⟨1|ψ⟩?", "0.6",
                    "⟨1| seleciona a parte em |1⟩.", "⟨1|ψ⟩ = 0,6.",
                    trap = "0.8", trapWhy = "0,8 é a amplitude em |0⟩. O bra ⟨1| projeta em |1⟩.")),
            real(Scene.Light, "Polarizadores", "A amplitude de um fóton diagonal passar por um filtro horizontal é ⟨H|D⟩ = 1/√2.",
                "⟨H|D⟩ = 1/√2")
        ),
        iso("Quanto vale ⟨0|1⟩?", "0",
            "Estados da base são ortogonais: não há parte de |1⟩ em |0⟩.",
            "1" to "⟨0|0⟩ = 1. Entre estados diferentes da base, o produto é 0.",
            "|1⟩" to "Um bra com um ket dá um número, não um estado.")
    ) { a, b ->
        plug("|ψ⟩ = $a|0⟩ + $b|1⟩ (sem normalizar). Calcule ⟨ψ|ψ⟩.", "${a * a + b * b}",
            "⟨ψ|ψ⟩ = $a² + $b².", "${a * a} + ${b * b} = ${a * a + b * b}.",
            trap = "${a + b}", trapWhy = "Cada amplitude entra ao quadrado.")
    },

    guide("schrodinger-time", """i\hbar\frac{\partial\psi}{\partial t}=H\psi""", "iℏ ∂ψ/∂t = Hψ",
        "A energia determina como o estado muda no tempo. Num estado de energia definida, só a fase gira, com frequência E/h.",
        listOf(sym("ψ", "o estado ou função de onda"), sym("H", "o operador energia, o hamiltoniano"),
            sym("ℏ", "a constante de Planck reduzida"), sym("t", "o tempo", "s"), sym("i", "a unidade imaginária")),
        listOf(
            step("""H\psi=E\psi""", "Se o estado tem energia definida E..."),
            step("""\psi(t)=\psi(0)e^{-iEt/\hbar}""", "...ele só ganha uma fase que gira."),
            step("""|\psi(t)|^{2}=|\psi(0)|^{2}""", "A fase tem módulo 1. As probabilidades ficam paradas."),
            step("""f=\frac{E}{h}""", "A fase gira E/h vezes por segundo. Diferenças de energia viram frequências medíveis.")
        ),
        listOf(
            real(Scene.Lab, "Relógio atômico", "A definição do segundo usa a frequência da transição do césio: 9 192 631 770 Hz, que vem de ΔE/h.",
                "f = ΔE/h",
                plug("Um relógio de césio conta 9 192 631 770 oscilações por segundo. Quantas oscilações em 2 s?", "18385263540",
                    "Multiplique a frequência pelo tempo.", "9 192 631 770 × 2 = 18 385 263 540.")),
            real(Scene.Phone, "GPS", "Satélites GPS carregam relógios atômicos. Um erro de 1 µs daria cerca de 300 m de erro na posição.",
                "300 000 000 m/s × 1e-6 s = 300 m"),
            real(Scene.Computer, "Qubits supercondutores", "Pulsos de micro-ondas na frequência ΔE/h giram o estado do qubit de forma controlada.",
                "f ≈ 5 GHz")
        ),
        iso("Num estado de energia definida, como |ψ(x, t)|² muda com o tempo?", "Não muda",
            "A evolução só multiplica ψ por uma fase de módulo 1.",
            "Gira junto com a fase" to "A fase gira, mas o módulo ao quadrado apaga a fase.",
            "Cai exponencialmente" to "A evolução é unitária. A probabilidade total não se perde.")
    ) { a, _ ->
        plug("Um estado tem E = $a × 10⁻²⁰ J. Com que frequência sua fase gira? Use f = E/h.", "${a}e-20/h",
            "Use f = E/h = ${a}e-20 ÷ 6,626e-34.", "f ≈ ${br(a / 6.62607015)} × 10¹⁴ Hz.", unit = "Hz",
            trap = "h/(${a}e-20)", trapWhy = "f é a energia dividida por h.", tolerance = 5e-3)
    },

    guide("schrodinger-stationary", """E_{n}=\frac{n^{2}h^{2}}{8mL^{2}}""", "Eₙ = n² h² / (8 m L²)",
        "Numa caixa, só cabem ondas com nós nas paredes. Por isso a energia vem em degraus proporcionais a n².",
        listOf(sym("Eₙ", "a energia do nível n", "J"), sym("n", "o número quântico, 1, 2, 3…"),
            sym("h", "a constante de Planck"), sym("m", "a massa da partícula", "kg"), sym("L", "a largura da caixa", "m")),
        listOf(
            step("""L=n\frac{\lambda}{2}""", "Cabe um número inteiro de meias ondas na caixa."),
            step("""p=\frac{h}{\lambda}=\frac{nh}{2L}""", "Use de Broglie para achar o momento."),
            step("""E=\frac{p^{2}}{2m}""", "Energia cinética em termos do momento."),
            step("""E_{n}=n^{2}E_{1}""", "Os níveis crescem como 1, 4, 9, 16… e caixas menores têm níveis mais altos.")
        ),
        listOf(
            real(Scene.Light, "Pontos quânticos", "Nanocristais em telas QLED: cristais menores têm níveis mais separados e emitem luz mais azul. O tema deu o Nobel de Química de 2023.",
                "E ∝ 1/L²",
                plug("Um ponto quântico tem L reduzido à metade. Por quanto a energia do nível 1 é multiplicada?", "4",
                    "E ∝ 1/L². Com L/2, o denominador fica 1/4 do original.", "A energia é multiplicada por 4.",
                    trap = "2", trapWhy = "L aparece ao quadrado: metade da largura dá 4 vezes a energia.")),
            real(Scene.Lab, "Corantes", "Moléculas longas de corante se comportam como caixas. Cadeias maiores absorvem cores de maior comprimento de onda.",
                "L maior ⇒ ΔE menor")
        ),
        iso("Quanto vale E₂/E₁ numa caixa infinita?", "4",
            "Eₙ ∝ n², então E₂/E₁ = 2²/1².",
            "2" to "A energia cresce com n², não com n.",
            "1/4" to "Níveis mais altos têm mais energia, não menos.")
    ) { a, _ ->
        plug("Numa caixa infinita, calcule Eₙ/E₁ para n = $a.", "${a * a}",
            "Use Eₙ = n² E₁.", "$a² = ${a * a}.", trap = "${2 * a}", trapWhy = "A razão é n², não 2n.")
    },

    guide("tunneling", """T\approx e^{-2\kappa L}\quad\kappa=\frac{\sqrt{2m(V_{0}-E)}}{\hbar}""", "T ≈ e^(−2κL) · κ = √(2m(V₀ − E)) / ℏ",
        "Uma partícula pode atravessar uma barreira mais alta que sua energia. A chance cai exponencialmente com a largura.",
        listOf(sym("T", "a probabilidade de transmissão"), sym("κ", "a taxa de decaimento da onda dentro da barreira", "1/m"),
            sym("L", "a largura da barreira", "m"), sym("V₀", "a altura da barreira", "J"), sym("E", "a energia da partícula", "J")),
        listOf(
            step("""E<V_{0}""", "Classicamente, a partícula seria refletida sempre."),
            step("""\psi\propto e^{-\kappa x}""", "Dentro da barreira a onda não some: ela decai."),
            step("""T\approx e^{-2\kappa L}""", "O que sobra do outro lado é a chance de atravessar."),
            step("""L\to2L\Rightarrow T\to T^{2}""", "Dobrar a largura eleva T ao quadrado. 1% vira 0,01%.")
        ),
        listOf(
            real(Scene.Computer, "Pendrive e SSD", "Memórias flash gravam bits empurrando elétrons através de um óxido fino por tunelamento.",
                "óxido de poucos nm ⇒ T controlável"),
            real(Scene.Lab, "Microscópio de tunelamento", "A corrente muda cerca de 10 vezes a cada 0,1 nm de distância. Essa sensibilidade mostra átomos individuais.",
                "corrente ∝ e^(−2κd)",
                plug("No microscópio, a chance de tunelar é T = 0,01. Se a distância até a amostra dobra, qual o T aproximado?", "0.0001",
                    "Com 2L, T vira e^(−4κL) = T².", "0,01² = 0,0001.",
                    trap = "0.005", trapWhy = "O efeito é exponencial: dobrar L eleva T ao quadrado, não divide por 2.")),
            real(Scene.Space, "Fusão no Sol", "Prótons no Sol não têm energia para vencer a repulsão elétrica. Eles fundem por tunelamento, e por isso o Sol brilha.",
                "T pequeno × muitos prótons")
        ),
        iso("Se a barreira fica mais larga, a transmissão:", "Cai exponencialmente",
            "L está no expoente de e^(−2κL).",
            "Cai pela metade" to "A queda não é linear. Dobrar L eleva T ao quadrado.",
            "Aumenta" to "Barreira maior, menos chance de atravessar.")
    ) { a, _ ->
        plug("Uma barreira tem T = 1/$a. Se a largura dobra, qual o novo T?", "1/${a * a}",
            "Com 2L, T vira T².", "(1/$a)² = 1/${a * a}.",
            trap = "1/${2 * a}", trapWhy = "O decaimento é exponencial: o novo T é o quadrado do antigo.")
    },

    guide("spin", """P(+)=\cos^{2}\frac{\theta}{2}\quad S_{z}=\pm\frac{\hbar}{2}""", "P(+) = cos²(θ/2) · S_z = ±ℏ/2",
        "O spin do elétron, medido em qualquer eixo, dá só +ℏ/2 ou −ℏ/2. A chance de + depende da metade do ângulo entre os eixos.",
        listOf(sym("S_z", "o spin medido no eixo z", "J·s"), sym("θ", "o ângulo entre o eixo preparado e o eixo medido"),
            sym("P(+)", "a probabilidade de obter +ℏ/2"), sym("ℏ", "a constante de Planck reduzida")),
        listOf(
            step("""S_{z}=+\frac{\hbar}{2}\ ou\ -\frac{\hbar}{2}""", "Só dois resultados, em qualquer eixo."),
            step("""\theta=0°\Rightarrow P=1""", "Mesmo eixo: o resultado se repete com certeza."),
            step("""\theta=90°\Rightarrow P=\cos^{2}45°=\frac{1}{2}""", "Eixos perpendiculares: 50% de cada."),
            step("""\theta=180°\Rightarrow P=0""", "Eixo oposto: nunca dá +.")
        ),
        listOf(
            real(Scene.Health, "Ressonância magnética", "Os spins dos prótons do corpo giram em torno do campo a cerca de 64 MHz num ímã de 1,5 T. O aparelho lê esse sinal.",
                "f = 42,58 MHz/T × 1,5 T ≈ 64 MHz"),
            real(Scene.Computer, "Disco rígido", "Cabeças de leitura usam a magnetorresistência gigante, um efeito de spin que rendeu o Nobel de 2007.",
                "resistência depende do spin"),
            real(Scene.Lab, "Stern-Gerlach", "Em 1922, um feixe de átomos de prata se dividiu em apenas duas manchas, revelando os dois valores do spin.",
                "duas manchas = ±ℏ/2",
                plug("Spin preparado em +z e medido num eixo a 60°. Qual P(+)? Use cos 30° = √3/2.", "3/4",
                    "Use cos²(θ/2) com θ/2 = 30°.", "(√3/2)² = 3/4.",
                    trap = "1/4", trapWhy = "1/4 é cos² 60°. Use metade do ângulo."))
        ),
        iso("Medindo S_z de um elétron, quais resultados são possíveis?", "+ℏ/2 ou −ℏ/2",
            "O spin do elétron é quantizado em dois valores.",
            "Qualquer valor entre −ℏ/2 e +ℏ/2" to "Os valores intermediários aparecem só como médias de muitas medidas.",
            "0, ℏ/2 ou ℏ" to "Esses valores correspondem a partículas de spin diferente.")
    ) { a, b ->
        val choices = listOf(0 to "1", 60 to "3/4", 90 to "1/2", 120 to "1/4", 180 to "0")
        val (angle, result) = choices[(a + b) % choices.size]
        val wrong = listOf("1", "1/4", "0", "1/4", "1")[(a + b) % choices.size]
        plug("Spin preparado em +z e medido num eixo a $angle°. Qual P(+)?", result,
            "Calcule o meio-ângulo: ${angle / 2}°.", "P = cos²(${angle / 2}°) = $result.",
            trap = wrong, trapWhy = "Use cos²(θ/2), com metade do ângulo.")
    },

    guide("entanglement", """|s\rangle=\frac{|00\rangle+|11\rangle}{\sqrt{2}}""", "|s⟩ = (|00⟩ + |11⟩)/√2",
        "Dois qubits emaranhados formam um estado único. Cada resultado sozinho é aleatório, mas os dois sempre concordam.",
        listOf(sym("|s⟩", "o estado emaranhado do par"), sym("|00⟩", "os dois qubits em 0"), sym("|11⟩", "os dois qubits em 1"),
            sym("1/√2", "a amplitude de cada combinação")),
        listOf(
            step("""P(00)=\frac{1}{2}\quad P(11)=\frac{1}{2}""", "Cada combinação aparece metade das vezes."),
            step("""P(01)=P(10)=0""", "Resultados diferentes nunca aparecem."),
            step("""A=0\Rightarrow B=0""", "Se A mediu 0, B certamente mede 0."),
            step("""sem\ sinal\ mais\ rápido\ que\ a\ luz""", "Cada lado vê 50% aleatório. A correlação só aparece ao comparar as listas.")
        ),
        listOf(
            real(Scene.Space, "Satélite Micius", "Em 2017, um satélite chinês distribuiu pares emaranhados para estações a mais de 1 200 km de distância.",
                "correlação mantida a 1 200 km"),
            real(Scene.Money, "Criptografia quântica", "Pares emaranhados geram chaves secretas. Qualquer espionagem perturba as correlações e é detectada.",
                "chaves iguais nos dois lados",
                plug("Par (|00⟩ + |11⟩)/√2. Em 1000 medições, em quantas os dois resultados são iguais?", "1000",
                    "P(00) + P(11) = 1/2 + 1/2 = 1.", "Os resultados concordam em todas as 1000 medições.",
                    trap = "500", trapWhy = "500 é só a contagem de 00. Some também os casos 11.")),
            real(Scene.Lab, "Nobel de 2022", "Aspect, Clauser e Zeilinger mostraram que essas correlações violam as desigualdades de Bell.",
                "correlação > limite clássico")
        ),
        iso("No estado (|00⟩ + |11⟩)/√2, se A mede 0, B mede:", "0 com certeza",
            "Só as combinações 00 e 11 têm amplitude.",
            "1 com certeza" to "A combinação 01 tem amplitude zero.",
            "0 ou 1 com 50%" to "Isso vale antes de A medir. Depois, B fica determinado.")
    ) { a, _ ->
        plug("Um par tem amplitudes √($a/10) para 00 e √(${10 - a}/10) para 11. Qual P(11)?", "${10 - a}/10",
            "Selecione a amplitude de 11: √(${10 - a}/10).", "P = (√(${10 - a}/10))² = ${10 - a}/10.",
            trap = "sqrt(${10 - a}/10)", trapWhy = "Eleve a amplitude ao quadrado.")
    },

    guide("decoherence", """\rho_{01}^{novo}=\rho_{01}\langle e_{1}|e_{0}\rangle""", "ρ₀₁(novo) = ρ₀₁ · ⟨e₁|e₀⟩",
        "Quando o ambiente registra informação sobre o sistema, a coerência é multiplicada pela sobreposição dos registros e encolhe.",
        listOf(sym("ρ₀₁", "a coerência, que permite interferência"), sym("⟨e₁|e₀⟩", "a sobreposição entre os registros deixados no ambiente"),
            sym("ρ₀₁(novo)", "a coerência depois da interação")),
        listOf(
            step("""\rho_{01}=\frac{1}{2}""", "Uma superposição igual começa com coerência 1/2."),
            step("""\langle e_{1}|e_{0}\rangle=0{,}8""", "O ambiente quase não distingue os ramos."),
            step("""\rho_{01}=0{,}4""", "A coerência diminui um pouco."),
            step("""\langle e_{1}|e_{0}\rangle\to0""", "Se o ambiente distingue tudo, a interferência some e o sistema parece clássico.")
        ),
        listOf(
            real(Scene.Computer, "Computadores quânticos", "Qubits supercondutores ficam a cerca de 10 mK, mais frios que o espaço, para atrasar a decoerência.",
                "coerência por ~100 µs",
                plug("A coerência começa em 0,5 e cada interação a multiplica por 0,8. Quanto vale depois de 2 interações?", "0.32",
                    "Multiplique duas vezes por 0,8: 0,5 × 0,8 × 0,8.", "0,5 × 0,64 = 0,32.",
                    trap = "0.1", trapWhy = "A decoerência multiplica, não subtrai 0,2 por vez.")),
            real(Scene.Nature, "Gato de Schrödinger", "Objetos grandes trocam informação com bilhões de partículas por segundo. A superposição desaparece em tempos minúsculos.",
                "⟨e₁|e₀⟩ ≈ 0 quase instantaneamente"),
            real(Scene.Lab, "Relógios atômicos", "Átomos presos no vácuo e protegidos de luz mantêm a coerência por segundos.",
                "isolamento ⇒ coerência longa")
        ),
        iso("O que a decoerência apaga?", "Os termos de coerência fora da diagonal",
            "As probabilidades da diagonal continuam. A capacidade de interferir desaparece.",
            "As probabilidades da diagonal" to "As probabilidades dos resultados se mantêm.",
            "Nada: ela aumenta a interferência" to "Ela reduz a interferência.")
    ) { a, _ ->
        plug("A coerência vale 1/2 e a sobreposição do ambiente é 1/$a. Qual a nova coerência?", "1/${2 * a}",
            "Multiplique 1/2 por 1/$a.", "Resultado: 1/${2 * a}.",
            trap = "1/2+1/$a", trapWhy = "A sobreposição multiplica a coerência.")
    },

    guide("oscillator", """E_{n}=\hbar\omega\left(n+\frac{1}{2}\right)""", "Eₙ = ℏω (n + ½)",
        "Um oscilador quântico tem degraus de energia igualmente espaçados por ℏω, e nunca fica com energia zero.",
        listOf(sym("Eₙ", "a energia do nível n", "eV"), sym("ℏω", "o espaçamento entre níveis", "eV"),
            sym("n", "o número do nível, 0, 1, 2…"), sym("½", "o termo de ponto zero")),
        listOf(
            step("""E_{0}=\frac{1}{2}\hbar\omega""", "Mesmo no nível mais baixo sobra energia: o ponto zero."),
            step("""E_{1}=\frac{3}{2}\hbar\omega""", "O próximo nível tem um quantum a mais."),
            step("""E_{n+1}-E_{n}=\hbar\omega""", "Todos os degraus têm a mesma altura."),
            step("""E_{2}=2{,}5\hbar\omega""", "Para n = 2: 2 + 0,5 = 2,5 quanta.")
        ),
        listOf(
            real(Scene.Nature, "Efeito estufa", "As vibrações do CO₂ são osciladores quânticos. O degrau delas casa com o infravermelho emitido pela Terra.",
                "ℏω ≈ 0,08 eV ⇒ λ ≈ 15 µm"),
            real(Scene.Lab, "Espectroscopia infravermelha", "Cada ligação química vibra com seu próprio ℏω. O espectro identifica moléculas em perícia, bafômetros e indústria.",
                "pico em ℏω",
                plug("Um modo vibra com ℏω = 0,3 eV. Qual a energia do nível n = 2?", "0.75",
                    "Use E = ℏω(n + ½) = 0,3 × 2,5.", "E = 0,75 eV.", unit = "eV",
                    trap = "0.6", trapWhy = "Não esqueça o ½ do ponto zero.")),
            real(Scene.Computer, "Qubits transmon", "Muitos qubits supercondutores são osciladores ligeiramente desiguais. Os dois níveis mais baixos viram 0 e 1.",
                "|0⟩ = E₀, |1⟩ = E₁")
        ),
        iso("Qual é a diferença entre dois níveis vizinhos?", "ℏω, sempre a mesma",
            "(n + 1 + ½) − (n + ½) = 1 quantum.",
            "ℏω/2" to "ℏω/2 é a energia de ponto zero, não o degrau.",
            "Cresce com n" to "No oscilador harmônico os degraus são iguais.")
    ) { a, b ->
        plug("Um oscilador tem ℏω = ${b}/10 eV. Qual a energia do nível n = $a?", "$b/10*($a+0.5)",
            "Use E = ℏω(n + ½) = ${br(b / 10.0)} × ${br(a + 0.5)}.", "E = ${br(b / 10.0 * (a + 0.5))} eV.", unit = "eV",
            trap = "$b/10*$a", trapWhy = "Some o ½ do ponto zero.", tolerance = 1e-6)
    },

    guide("hydrogen", """E_{n}=-\frac{13{,}6\,eV}{n^{2}}""", "Eₙ = −13,6 eV / n²",
        "O elétron do hidrogênio só pode ter energias −13,6/n² eV. Saltos entre níveis emitem fótons de cores fixas.",
        listOf(sym("Eₙ", "a energia do nível n", "eV"), sym("n", "o número quântico principal, 1, 2, 3…"),
            sym("13,6 eV", "a energia de ionização do hidrogênio")),
        listOf(
            step("""E_{1}=-13{,}6""", "O nível fundamental é o mais ligado."),
            step("""E_{2}=-3{,}4\quad E_{3}=-1{,}51""", "Divida por n²."),
            step("""\Delta E=E_{3}-E_{2}=1{,}89""", "Cair de 3 para 2 libera 1,89 eV."),
            step("""\lambda=\frac{1240}{1{,}89}\approx656\,nm""", "Essa é a linha vermelha do hidrogênio.")
        ),
        listOf(
            real(Scene.Space, "Nebulosas vermelhas", "O brilho rosado de nebulosas vem da linha de 656 nm, emitida quando elétrons do hidrogênio caem do nível 3 para o 2.",
                "ΔE = 13,6(1/4 − 1/9) ≈ 1,89 eV",
                plug("Calcule a energia liberada na transição n = 3 → n = 2 do hidrogênio.", "13.6*(1/4-1/9)",
                    "Use ΔE = 13,6 × (1/2² − 1/3²).", "13,6 × 5/36 ≈ 1,89 eV.", unit = "eV",
                    trap = "13.6/9", trapWhy = "Esse é só |E₃|. Subtraia as energias dos dois níveis.", tolerance = 1e-4)),
            real(Scene.Light, "Letreiros de neon", "Cada gás tem seus níveis e, portanto, suas cores. O espectro funciona como impressão digital.",
                "cores = diferenças de níveis"),
            real(Scene.Lab, "Hélio no Sol", "O hélio foi descoberto em 1868 por uma linha no espectro do Sol, antes de ser encontrado na Terra.",
                "linha desconhecida ⇒ novo elemento")
        ),
        iso("Quanta energia é preciso fornecer para ionizar o hidrogênio a partir do nível 1?", "13,6 eV",
            "É preciso levar o elétron de −13,6 eV até 0.",
            "−13,6 eV" to "−13,6 eV é a energia do nível. A energia a fornecer é positiva.",
            "6,8 eV" to "Isso levaria a −6,8 eV, que não é um nível permitido nem a ionização.")
    ) { a, b ->
        val low = a % 3 + 1
        val high = low + b % 2 + 1
        plug("No hidrogênio, qual a energia liberada na transição n = $high → n = $low?", "13.6*(1/${low * low}-1/${high * high})",
            "Use ΔE = 13,6 × (1/$low² − 1/$high²).", "ΔE ≈ ${br(13.6 * (1.0 / (low * low) - 1.0 / (high * high)))} eV.", unit = "eV",
            trap = "13.6/${high * high}", trapWhy = "Subtraia as energias dos dois níveis.", tolerance = 1e-6)
    },

    guide("perturbation", """\Delta E^{(1)}=\langle n|V|n\rangle""", "ΔE⁽¹⁾ = ⟨n|V|n⟩",
        "Uma pequena perturbação V corrige cada nível pelo valor médio de V no estado original.",
        listOf(sym("ΔE⁽¹⁾", "a correção de primeira ordem na energia", "eV"), sym("V", "a perturbação, uma pequena energia extra"),
            sym("|n⟩", "o estado original, sem perturbação")),
        listOf(
            step("""H=H_{0}+V""", "Separe o problema fácil (H₀) da pequena mudança (V)."),
            step("""\Delta E^{(1)}=\langle n|V|n\rangle""", "Calcule a média de V no estado conhecido."),
            step("""E\approx E_{n}+\Delta E^{(1)}""", "Some a correção à energia original."),
            step("""10+0{,}3=10{,}3""", "Exemplo: nível de 10 eV com correção de +0,3 eV.")
        ),
        listOf(
            real(Scene.Space, "Campo magnético do Sol", "Num campo magnético, linhas espectrais se dividem (efeito Zeeman). Astrônomos medem a divisão para saber o campo nas manchas solares.",
                "ΔE = μ_B B mₗ"),
            real(Scene.Lab, "Efeito Stark", "Um campo elétrico desloca os níveis de átomos e moléculas. A correção de primeira ordem prevê o deslocamento.",
                "ΔE = ⟨n|V|n⟩",
                plug("Um nível tem 10 eV e ⟨n|V|n⟩ = −0,25 eV. Qual a energia aproximada?", "9.75",
                    "Use E ≈ Eₙ + ΔE⁽¹⁾ = 10 + (−0,25).", "E ≈ 9,75 eV.", unit = "eV",
                    trap = "10.25", trapWhy = "A correção é negativa. Some com o sinal.")),
            real(Scene.Computer, "Ajuste de qubits", "Engenheiros usam perturbação para prever como pequenos campos deslocam a frequência dos qubits.",
                "Δf = ΔE/h")
        ),
        iso("A correção de primeira ordem é:", "O valor médio de V no estado original",
            "ΔE⁽¹⁾ = ⟨n|V|n⟩.",
            "A energia original vezes V" to "A correção se soma à energia, não a multiplica.",
            "Sempre positiva" to "O sinal depende de V.")
    ) { a, b ->
        plug("Uma energia de $a eV recebe correção de +$b/10 eV. Qual a energia aproximada?", "$a+$b/10",
            "Use E ≈ Eₙ + ΔE⁽¹⁾.", "$a + ${br(b / 10.0)} = ${br(a + b / 10.0)} eV.", unit = "eV",
            trap = "$a*$b/10", trapWhy = "A correção se soma à energia.")
    },

    guide("quantum-fields", """\Delta E=\hbar\omega\quad N=\frac{P}{hf}""", "ΔE = ℏω por quantum · N = P / (hf)",
        "Num campo quântico, cada modo ganha energia em pacotes de ℏω. Os pacotes são partículas, como fótons.",
        listOf(sym("ΔE", "a energia de um quantum do campo", "J"), sym("ℏω", "a energia de cada excitação"),
            sym("N", "o número de quanta por segundo"), sym("P", "a potência do feixe", "W")),
        listOf(
            step("""|0\rangle""", "O vácuo é o estado sem excitações, mas ainda com flutuações."),
            step("""|1\rangle:\ +\hbar\omega""", "Uma excitação é um fóton."),
            step("""|n\rangle:\ +n\hbar\omega""", "n fótons somam n quanta."),
            step("""N=\frac{P}{hf}""", "Um feixe de potência P emite P/(hf) fótons por segundo.")
        ),
        listOf(
            real(Scene.Light, "Laser de apresentação", "Um laser de 1 mW emite cerca de 3 × 10¹⁵ fótons por segundo. Parece contínuo porque são muitíssimos.",
                "N = 1e-3 ÷ 3,2e-19 ≈ 3,1 × 10¹⁵",
                plug("Um laser de 1 mW emite fótons de 3,2e-19 J. Quantos fótons por segundo?", "1e-3/3.2e-19",
                    "Use N = P/E = 1e-3 ÷ 3,2e-19.", "N ≈ 3,1 × 10¹⁵ fótons por segundo.",
                    trap = "3.2e-19/1e-3", trapWhy = "Divida a potência pela energia de cada fóton.", tolerance = 1e-4)),
            real(Scene.Lab, "Bóson de Higgs", "Em 2012, o LHC detectou o Higgs, uma excitação do campo de Higgs, com cerca de 125 GeV.",
                "partícula = excitação do campo"),
            real(Scene.Space, "Telescópios", "Câmeras astronômicas contam fótons individuais vindos de galáxias muito distantes.",
                "imagem = contagem de quanta")
        ),
        iso("Acrescentar um quantum a um modo de frequência ω soma:", "ℏω",
            "Cada excitação tem a mesma energia.",
            "ℏω/2" to "ℏω/2 é a energia de ponto zero do modo.",
            "2ℏω" to "Um quantum soma uma unidade ℏω.")
    ) { a, b ->
        plug("Um laser de $a mW emite fótons de $b × 10⁻¹⁹ J. Quantos fótons por segundo?", "${a}e-3/(${b}e-19)",
            "Use N = P/E = ${a}e-3 ÷ ${b}e-19.", "N ≈ ${br(a.toDouble() / b)} × 10¹⁶ fótons por segundo.",
            trap = "${b}e-19/(${a}e-3)", trapWhy = "Divida a potência pela energia de cada fóton.", tolerance = 1e-4)
    },

    guide("qubit", """|\psi\rangle=a|0\rangle+b|1\rangle\quad P(1)=|b|^{2}""", "|ψ⟩ = a|0⟩ + b|1⟩ · P(1) = |b|²",
        "Um qubit é uma superposição de 0 e 1. A medida dá 1 com probabilidade |b|². n qubits precisam de 2ⁿ amplitudes.",
        listOf(sym("|ψ⟩", "o estado do qubit"), sym("a", "a amplitude de |0⟩"), sym("b", "a amplitude de |1⟩"),
            sym("P(1)", "a probabilidade de medir 1")),
        listOf(
            step("""|\psi\rangle=a|0\rangle+b|1\rangle""", "Duas amplitudes complexas."),
            step("""P(0)=|a|^{2}\quad P(1)=|b|^{2}""", "Regra de Born."),
            step("""n\ qubits\Rightarrow2^{n}\ amplitudes""", "Cada qubit dobra o tamanho do estado."),
            step("""2^{50}\approx10^{15}""", "50 qubits já exigem mais de um quatrilhão de amplitudes num computador comum.")
        ),
        listOf(
            real(Scene.Computer, "Simular qubits", "Simular n qubits num computador clássico exige guardar 2ⁿ números. É por isso que computadores quânticos são promissores.",
                "2ⁿ amplitudes",
                plug("Quantas amplitudes descrevem 10 qubits?", "1024",
                    "Cada qubit dobra o número de amplitudes: 2¹⁰.", "2¹⁰ = 1024.",
                    trap = "20", trapWhy = "20 é 2 × 10. O crescimento é exponencial: 2¹⁰.")),
            real(Scene.Money, "Criptografia", "O algoritmo de Shor poderia quebrar a criptografia RSA. Em 2024, o NIST publicou padrões de criptografia pós-quântica.",
                "fatorar N grande ⇒ quebra RSA"),
            real(Scene.Lab, "Química quântica", "Qubits podem representar moléculas naturalmente, ajudando a projetar catalisadores e baterias.",
                "elétrons ↔ qubits",
                plug("Um qubit tem b = 0,8. Qual a probabilidade de medir 1?", "0.64",
                    "Use P(1) = |b|².", "0,8² = 0,64.",
                    trap = "0.8", trapWhy = "Eleve a amplitude ao quadrado."))
        ),
        iso("Medir um qubit em (|0⟩ + |1⟩)/√2 dá:", "0 ou 1, cada um com 50%",
            "A medida sempre dá um bit. As chances são |1/√2|² = 1/2.",
            "0,5" to "A medida não dá valores intermediários.",
            "Os dois ao mesmo tempo" to "A superposição existe antes da medida. A medida mostra um único resultado.")
    ) { a, b ->
        val n = a + b
        plug("Quantas amplitudes são necessárias para descrever $n qubits?", "${1 shl n}",
            "Cada qubit dobra a contagem: 2^$n.", "2^$n = ${1 shl n}.",
            trap = "${2 * n}", trapWhy = "O número de amplitudes cresce como 2ⁿ, não como 2n.")
    }
)
