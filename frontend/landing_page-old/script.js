const chatBody = document.getElementById('chat-body');
const nextButton = document.getElementById('next-button');
const termsContainer = document.getElementById('terms-container');

let currentStep = 0;
const formData = {};

const steps = [
    {
        message: "Olá! Bem-vindo(a) à SJF Juristas. Para começarmos sua simulação, qual valor de empréstimo você deseja ?",
        input: {
            type: 'number',
            name: 'loanValue',
            placeholder: 'Ex: 5000',
            label: 'Valor desejado (R$):',
            min: 100, 
            max: 50000 
        },
        validation: (value) => value && value >= 100 && value <= 50000, // Valores meramente ilustrativos. Futuramente vai ser implementado com base em valores guardados no banco de dados.
        errorMessage: 'Por favor, insira um valor válido entre R$100 e R$50.000.'
    },
    {
        message: 'Ótimo! Agora, por favor, digite seu nome completo:',
        input: {
            type: 'text',
            name: 'fullName',
            placeholder: 'Seu nome completo',
            label: 'Nome Completo:',
            pattern: '^[A-Za-zÀ-ú\\s]+$',
            minlength: 5
        },
        validation: (value) => value && value.length >= 5 && /^[A-Za-zÀ-ú\s]+$/.test(value), 
        errorMessage: 'Por favor, insira um nome completo válido (mínimo 5 caracteres, apenas letras e espaços).'
    },
    {
        message: 'Certo! Para darmos andamento, qual é o seu CPF?',
        input: {
            type: 'text',
            name: 'cpf',
            placeholder: '000.000.000-00',
            label: 'CPF:',
            mask: '000.000.000-00'
        },
        validation: (value) => {
            if (!value) return false;
            const cpf = value.replace(/\D/g, '');
            return cpf.length === 11 && /^\d{11}$/.test(cpf);
        },
        errorMessage: 'Por favor, insira um CPF válido com 11 dígitos.'
    },
    {
        message: 'Para entrarmos em contato, qual seu melhor e-mail?',
        input: {
            type: 'email',
            name: 'email',
            placeholder: 'seu.email@exemplo.com',
            label: 'E-mail:'
        },
        validation: (value) => value && /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value), 
        errorMessage: 'Por favor, insira um e-mail válido.'
    },
    {
        message: 'E por fim, seu número de WhatsApp com DDD, por favor:',
        input: {
            type: 'tel',
            name: 'whatsapp',
            placeholder: '(XX) 9XXXX-XXXX',
            label: 'WhatsApp:',
            mask: '(00) 00000-0000'
        },
         validation: (value) => {
            if (!value) return false;
            const whatsapp = value.replace(/\D/g, '');
            return whatsapp.length === 11 && /^\d{11}$/.test(whatsapp);
        },
        errorMessage: 'Por favor, insira um número de WhatsApp válido com DDD (11 dígitos).'
    },
    {
        message: 'Precisamos de algumas fotos dos seus documentos. Por favor, envie uma foto do seu RG/CNH (frente e verso) e um comprovante de residência.',
        input: [
            { type: 'file', name: 'rgCnhFront', label: 'RG/CNH - Frente:', accept: 'image/*', required: true },
            { type: 'file', name: 'rgCnhBack', label: 'RG/CNH - Verso:', accept: 'image/*', required: true },
            { type: 'file', name: 'proofOfAddress', label: 'Comprovante de Residência:', accept: 'image/*', required: true }
        ],
        validation: (files) => {
            return files && files.rgCnhFront && files.rgCnhBack && files.proofOfAddress; 
        },
        errorMessage: 'Por favor, envie todas as fotos dos documentos solicitados.'
    },
    {
        message: 'Por favor, leia e aceite nossos Termos de Uso e Política de Privacidade para prosseguir.',
        input: {
            type: 'checkbox',
            name: 'termsAccepted',
            label: 'Eu li e aceito os <a href="#" id="terms-link">Termos de Uso</a> e a <a href="#" id="privacy-link">Política de Privacidade</a>.' // IDs para JS
        },
        validation: (value) => value === true,
        errorMessage: 'Você deve aceitar os Termos de Uso e Política de Privacidade para continuar.'
    },
    {
        message: 'Pronto! Sua solicitação foi enviada com sucesso! Em breve entraremos em contato com sua proposta. Obrigado por escolher a Capitaleads!',
        input: null 
    }
]

function applyInputMasks() 
{
    const cpfInput = document.querySelector('input[name="cpf"]');
    if (cpfInput && steps[currentStep].input.name === 'cpf') {
        cpfInput.oninput = function() {
            let value = cpfInput.value.replace(/\D/g, ''); 
            if (value.length > 11) value = value.slice(0, 11);

            let maskedValue = '';
            if (value.length > 0) {
                maskedValue = value.substring(0,3);
                if (value.length > 3) maskedValue += '.' + value.substring(3,6);
                if (value.length > 6) maskedValue += '.' + value.substring(6,9);
                if (value.length > 9) maskedValue += '-' + value.substring(9,11);
            }
            cpfInput.value = maskedValue;
            formData['cpf'] = cpfInput.value; // Atualiza formData com valor mascarado
        };
    }

    const whatsappInput = document.querySelector('input[name="whatsapp"]');
    if (whatsappInput && steps[currentStep].input.name === 'whatsapp') {
        whatsappInput.oninput = function() {
            let value = whatsappInput.value.replace(/\D/g, ''); 
            if (value.length > 11) value = value.slice(0, 11);
            
            let maskedValue = '';
            if (value.length > 0) {
                maskedValue = '(' + value.substring(0,2);
                if (value.length > 2) maskedValue += ') ' + value.substring(2,7);
                if (value.length > 7) maskedValue += '-' + value.substring(7,11);
            }
            whatsappInput.value = maskedValue;
            formData['whatsapp'] = whatsappInput.value; // Atualiza formData com valor mascarado
        };
    }
}

function renderStep() 
{
    const step = steps[currentStep];
    termsContainer.style.display = 'none'; 
    chatBody.innerHTML = `<div class="chat-message">${step.message}</div>`;
    
    if (step.input) 
    {
        const inputGroup = document.createElement('div');
        inputGroup.className = 'chat-input-group';
        if (Array.isArray(step.input)) 
        { 
            step.input.forEach(inputConfig => {
                const labelElement = document.createElement('label'); 
                labelElement.textContent = inputConfig.label;
                const fileInput = document.createElement('input');
                fileInput.type = inputConfig.type;
                fileInput.name = inputConfig.name;
                fileInput.accept = inputConfig.accept;
                fileInput.required = inputConfig.required;
                fileInput.style.display = 'none'; 
                const customLabel = document.createElement('label');
                customLabel.htmlFor = inputConfig.name;
                customLabel.className = 'file-upload-label';
                customLabel.innerHTML = `Upload ${inputConfig.label.replace(':', '')}`;
                
                const filePreview = document.createElement('div');
                filePreview.className = 'file-preview';
                filePreview.id = `${inputConfig.name}-preview`;
                filePreview.textContent = formData[inputConfig.name] ? `Arquivo: ${formData[inputConfig.name].name}` : 'Nenhum arquivo selecionado.';
                fileInput.addEventListener('change', (event) => {
                    const fileName = event.target.files.length > 0 ? event.target.files[0].name : 'Nenhum arquivo selecionado.';
                    filePreview.textContent = `Arquivo: ${fileName}`;
                    formData[inputConfig.name] = event.target.files[0]; 
                });
                
                customLabel.appendChild(fileInput);
                inputGroup.appendChild(labelElement);
                inputGroup.appendChild(customLabel);
                inputGroup.appendChild(filePreview);
            });
        } 
        else if (step.input.type === 'checkbox') 
        {
            termsContainer.style.display = 'block'; 
            chatBody.innerHTML = `<div class="chat-message">${step.message}</div>`; 
            
            const checkboxContainer = document.createElement('div');
            checkboxContainer.className = 'checkbox-container';
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.name = step.input.name;
            checkbox.id = step.input.name;
            checkbox.checked = formData[step.input.name] || false;
            const labelElement = document.createElement('label'); 
            labelElement.htmlFor = step.input.name;
            labelElement.innerHTML = step.input.label;
            
            checkboxContainer.appendChild(checkbox);
            checkboxContainer.appendChild(labelElement);
            inputGroup.appendChild(checkboxContainer);
            checkbox.addEventListener('change', (event) => {
                formData[step.input.name] = event.target.checked;
            });
            
            
            const termsLinkStatic = document.getElementById('terms-link-static');
            const privacyLinkStatic = document.getElementById('privacy-link-static');
            if (termsLinkStatic) 
            {
                termsLinkStatic.onclick = (e) => { 
                    e.preventDefault();
                    alert('Conteúdo dos Termos de Uso (Exemplo). Aqui você colocaria um modal ou redirecionaria para uma página completa.');
                };
            }
            if (privacyLinkStatic)
            {
                privacyLinkStatic.onclick = (e) => { 
                    e.preventDefault();
                    alert('Conteúdo da Política de Privacidade (Exemplo). Aqui você colocaria um modal ou redirecionaria para uma página completa.');
                };
            }
            // Links dentro do label gerado dinamicamente
            const termsLinkDynamic = labelElement.querySelector('#terms-link');
            const privacyLinkDynamic = labelElement.querySelector('#privacy-link');
            if (termsLinkDynamic) 
            {
                termsLinkDynamic.addEventListener('click', (e) => {
                    e.preventDefault();
                    alert('Conteúdo dos Termos de Uso (Exemplo). Aqui você colocaria um modal ou redirecionaria para uma página completa.');
                });
            }
            if (privacyLinkDynamic) 
            {
                privacyLinkDynamic.addEventListener('click', (e) => {
                    e.preventDefault();
                    alert('Conteúdo da Política de Privacidade (Exemplo). Aqui você colocaria um modal ou redirecionaria para uma página completa.');
                });
            }
        } 
        else 
        {
            const labelElement = document.createElement('label'); 
            labelElement.textContent = step.input.label;
            const input = document.createElement('input');
            input.type = step.input.type;
            input.name = step.input.name;
            input.placeholder = step.input.placeholder;
            if (step.input.min) input.min = step.input.min;
            if (step.input.max) input.max = step.input.max;
            if (step.input.pattern) input.pattern = step.input.pattern;
            if (step.input.minlength) input.minLength = step.input.minlength;
            input.value = formData[step.input.name] || '';
            inputGroup.appendChild(labelElement);
            inputGroup.appendChild(input);
            input.addEventListener('input', (event) => {
                formData[step.input.name] = event.target.value;
                if (step.input.mask) 
                {
                    applyInputMasks();
                }
            });
        }
        chatBody.appendChild(inputGroup);
    }
        if (currentStep === steps.length - 1) 
        {
                nextButton.style.display = 'none'; 
                termsContainer.style.display = 'none';
            } 
            else 
            {
                nextButton.style.display = 'block';
                nextButton.textContent = 'Próximo';
            }
            if (step.input && step.input.mask)
            { 
                 applyInputMasks();
            }
        }

        nextButton.addEventListener('click', () => {
            const step = steps[currentStep];
            let isValid = true;
            let currentInputValue;

            // Limpa mensagens de erro anteriores
            const existingError = chatBody.querySelector('.error-message');
            if (existingError) 
            {
                existingError.remove();
            }

            if (step.input) 
                {
                if (Array.isArray(step.input)) 
                { 
                    const files = {};
                    let allFilesPresent = true;
                    step.input.forEach(inputConfig => {
                        files[inputConfig.name] = formData[inputConfig.name];
                        if (inputConfig.required && !formData[inputConfig.name]) {
                            allFilesPresent = false;
                        }
                    });

                    currentInputValue = files;
                     if (!allFilesPresent && step.validation === steps[5].validation) { 
                        isValid = false;
                    }

                } 
                else if (step.input.type === 'checkbox') 
                {
                    currentInputValue = formData[step.input.name];
                } 
                else 
                {
                    currentInputValue = formData[step.input.name];
                }

                if (step.validation && !step.validation(currentInputValue)) 
                {
                    isValid = false;
                }
            }
            
            if (Array.isArray(step.input) && step.validation === steps[5].validation && !step.validation(currentInputValue)) 
            {
                isValid = false;
            }


    if (isValid) 
    {
        if (currentStep < steps.length - 1) 
        {
            currentStep++;
            renderStep();
        } 
        else 
        {
            // ENVIO
        }
    } 
    else if (step.errorMessage) 
    {
         const errorDiv = document.createElement('div');
         errorDiv.className = 'chat-message error-message';
         errorDiv.style.backgroundColor = 'var(--danger-color)';
         errorDiv.style.color = 'var(--text-on-primary)';
         errorDiv.textContent = step.errorMessage;
         chatBody.appendChild(errorDiv);
    }
});

renderStep();