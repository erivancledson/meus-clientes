//quando eu solto a tecla ele vai verificar a senha1 e a senha 2 se são iguais e libera a senha3 que é a senha atual, para alterar. alterar senha de medico e paciente
$('.pass').keyup(function(){
	$('#senha1').val() === $('#senha2').val()
	    ? $('#senha3').removeAttr('readonly')
	    : $('#senha3').attr('readonly', 'readonly');
});