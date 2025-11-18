const API_URL = 'http://localhost:8080/flores';

// Carregar flores ao iniciar a página
document.addEventListener('DOMContentLoaded', () => {
    console.log('🌸 Aplicação iniciada!');
    carregarFlores();

    // Adicionar evento ao formulário
    document.getElementById('florForm').addEventListener('submit', salvarFlor);
});

// Função para carregar todas as flores
async function carregarFlores() {
    console.log('📥 Carregando flores da API...');
    try {
        const response = await fetch(API_URL);
        console.log('✅ Response Status:', response.status);
        
        const flores = await response.json();
        console.log('📦 Flores recebidas:', flores);
        console.log('📊 Quantidade de flores:', flores.length);

        const listaFlores = document.getElementById('listaFlores');

        if (flores.length === 0) {
            console.log('⚠️ Nenhuma flor encontrada');
            listaFlores.innerHTML = '<p class="vazio">Nenhuma flor cadastrada ainda. Adicione a primeira! 🌸</p>';
            return;
        }

        console.log('🎨 Renderizando', flores.length, 'flores');
        listaFlores.innerHTML = flores.map(flor => `
            <div class="flor-card">
                <h3>🌸 ${flor.nome}</h3>
                <p><strong>ID:</strong> ${flor.id}</p>
                <div class="flor-card-buttons">
                    <button class="btn btn-edit" onclick="editarFlor('${flor.id}', '${flor.nome}')">Editar</button>
                    <button class="btn btn-delete" onclick="deletarFlor('${flor.id}')">Deletar</button>
                </div>
            </div>
        `).join('');
        
        console.log('✅ Flores renderizadas com sucesso!');

    } catch (error) {
        console.error('❌ Erro ao carregar flores:', error);
        mostrarMensagem('Erro ao carregar flores: ' + error.message, 'erro');
    }
}

// Função para salvar (criar ou atualizar) flor
async function salvarFlor(event) {
    event.preventDefault();
    console.log('💾 Iniciando salvamento de flor...');

    const id = document.getElementById('florId').value;
    const nome = document.getElementById('florNome').value;

    console.log('📝 Dados do formulário - ID:', id, 'Nome:', nome);

    const flor = { nome };
    console.log('📤 Objeto flor a ser enviado:', flor);

    try {
        let response;

        if (id) {
            // Atualizar flor existente
            console.log('🔄 Atualizando flor existente...');
            response = await fetch(`${API_URL}/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(flor)
            });
        } else {
            // Criar nova flor
            console.log('➕ Criando nova flor...');
            response = await fetch(API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(flor)
            });
        }

        console.log('📡 Response Status:', response.status);
        console.log('📡 Response OK:', response.ok);

        if (response.ok) {
            const resultado = await response.json();
            console.log('✅ Flor salva com sucesso!', resultado);
            mostrarMensagem(id ? 'Flor atualizada com sucesso!' : 'Flor adicionada com sucesso!', 'sucesso');
            limparFormulario();
            
            console.log('🔄 Recarregando lista de flores...');
            await carregarFlores();
        } else {
            console.error('❌ Erro ao salvar - Status:', response.status);
            const errorText = await response.text();
            console.error('❌ Erro detalhado:', errorText);
            mostrarMensagem('Erro ao salvar flor', 'erro');
        }

    } catch (error) {
        console.error('❌ Exceção ao salvar flor:', error);
        mostrarMensagem('Erro ao salvar flor: ' + error.message, 'erro');
    }
}

// Função para editar flor
function editarFlor(id, nome) {
    console.log('✏️ Editando flor - ID:', id, 'Nome:', nome);
    document.getElementById('florId').value = id;
    document.getElementById('florNome').value = nome;

    // Mudar o título do formulário
    document.querySelector('.form-section h2').textContent = 'Editar Flor';

    // Scroll até o formulário
    document.querySelector('.form-section').scrollIntoView({ behavior: 'smooth' });
}

// Função para deletar flor
async function deletarFlor(id) {
    console.log('🗑️ Solicitação para deletar flor - ID:', id);
    
    if (!confirm('Tem certeza que deseja deletar esta flor?')) {
        console.log('❌ Deleção cancelada pelo usuário');
        return;
    }

    console.log('🗑️ Deletando flor...');
    
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE'
        });

        console.log('📡 Delete Response Status:', response.status);

        if (response.ok) {
            console.log('✅ Flor deletada com sucesso!');
            mostrarMensagem('Flor deletada com sucesso!', 'sucesso');
            await carregarFlores();
        } else {
            console.error('❌ Erro ao deletar - Status:', response.status);
            mostrarMensagem('Erro ao deletar flor', 'erro');
        }

    } catch (error) {
        console.error('❌ Exceção ao deletar flor:', error);
        mostrarMensagem('Erro ao deletar flor: ' + error.message, 'erro');
    }
}

// Função para cancelar edição
function cancelarEdicao() {
    console.log('🚫 Cancelando edição');
    limparFormulario();
}

// Função para limpar formulário
function limparFormulario() {
    console.log('🧹 Limpando formulário');
    document.getElementById('florId').value = '';
    document.getElementById('florNome').value = '';
    document.querySelector('.form-section h2').textContent = 'Adicionar Flor';
}

// Função para mostrar mensagens
function mostrarMensagem(texto, tipo) {
    console.log('💬 Mostrando mensagem:', tipo, '-', texto);
    
    // Remove mensagem anterior se existir
    const mensagemAnterior = document.querySelector('.mensagem');
    if (mensagemAnterior) {
        mensagemAnterior.remove();
    }

    const mensagem = document.createElement('div');
    mensagem.className = `mensagem ${tipo}`;
    mensagem.textContent = texto;

    document.querySelector('.form-section').insertBefore(
        mensagem,
        document.getElementById('florForm')
    );

    // Remove a mensagem após 3 segundos
    setTimeout(() => {
        mensagem.remove();
    }, 3000);
}