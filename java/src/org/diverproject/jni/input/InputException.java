package org.diverproject.jni.input;

import org.diverproject.util.UtilException;

/**
 * <h1>Input Exception</h1>
 *
 * <p>Esse tipo de exceção é como uma outra qualquer, ou seja, gera TryCatch obrigatório.
 * É utilizado garantir que determinados procedimentos não falhem causado danos ao sistema.
 * Quando detectado exceções desse tipo a thread que gerou ele será interrompido.</p>
 *
 * @see AsckarynException
 *
 * @author Andrew
 */

public class InputException extends UtilException
{
	/**
	 * Número de serialização desse tipo de exceção.
	 */
	private static final long serialVersionUID = 2926428005702877844L;

	/**
	 * Constrói uma nova exceção gerada pelo sistema para leitura dos eventos de entrada.
	 * @param message mensagem que será exibida quando a exceção for gerada.
	 */

	public InputException(String message)
	{
		super(message);
	}

	/**
	 * Constrói uma nova exceção gerada pelo sistema para leitura dos eventos de entrada.
	 * @param format string contendo o formato da mensagem que será exibida.
	 * @param args argumentos respectivos a formatação da mensagem.
	 */

	public InputException(String format, Object... args)
	{
		super(format, args);
	}

	/**
	 * Constrói uma nova exceção de utilitário sendo necessário definir a exceção.
	 * Nesse caso irá construir uma nova exceção a partir de uma exceção existente.
	 * Utilizando a mensagem dessa exceção como mensagem desta.
	 * @param e exceção do qual será considerada para criar uma nova.
	 */

	public InputException(Exception e)
	{
		super(e);
	}

	/**
	 * Constrói uma nova exceção gerada pelo sistema para leitura dos eventos de entrada.
	 * Nesse caso a mensagem será usada de uma exceção já criada, porém permite adicionar
	 * um determinado conteúdo extra como dados que será posicionado entre aspas.
	 * @param e exceção para usar a mensagem armazenada no mesmo como exceção.
	 * @param format string contendo o formato do conteúdo extra.
	 * @param args argumentos respectivos a formatação da mensagem.
	 */

	public InputException(Exception e, String format, Object... args)
	{
		super(e, format, args);
	}
}
