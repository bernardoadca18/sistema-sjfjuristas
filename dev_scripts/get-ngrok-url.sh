echo "Aguardando o Ngrok iniciar..."
sleep 5

NGROK_API_URL="http://ngrok:4040/api/tunnels"
OUTPUT_FILE="/output/ngrok_url.json"

echo "Buscando URL pública do Ngrok em: $NGROK_API_URL"

COUNTER=0

while [ $COUNTER -lt 60]; do
    PUBLIC_URL=$(curl -s $NGROK_API_URL | jq -r '.tunnels[0].public_url')

    if [ -n "$PUBLIC_URL" ] && [ "$PUBLIC_URL" != "null" ]; then
        echo "URL pública encontrada: $PUBLIC_URL"
        echo "{\"url\":\"${PUBLIC_URL}\"}" > $OUTPUT_FILE
        echo "URL salva em $OUTPUT_FILE"
        exit 0
    fi
    
    echo "Ainda não foi possível obter a URL... tentando novamente em 2 segundos."
    sleep 2
    COUNTER=$((COUNTER+2))
done

echo "Erro: Não foi possível obter a URL do Ngrok após 60 segundos."
echo "{\"error\":\"Falha ao obter URL do Ngrok\"}" > $OUTPUT_FILE
exit 1