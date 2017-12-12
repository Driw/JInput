package org.diverproject.jni.input;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Evento de A��o</h1>
 *
 * <p>Eventos desse tipo s�o usados (recomendado) pelos despachantes para um evento detectado.
 * Por exemplo, uma tecla clicada n�o deve ficar na lista de teclas clicadas para sempre.
 * Usado esse evento que dever� garantir a sua exist�ncia no sistema por um per�odo.</p>
 *
 * <p>Essas a��es devem possuir um valor que ser� usado para identifica��o a a��o executada.
 * Quanto a dura��o n�o h� um padr�o para tal, deve ser definido de acordo com a seu uso.
 * Possui tamb�m todas as informa��es de um Evento de Entrada como outro qualquer./<p>
 *
 * @see InputEvent
 *
 * @author Andrew
 */

public class ActionEvent extends InputEvent
{
	/**
	 * C�digo para identifica��o de uma a��o n�o identificada.
	 */
	public static final int IV_UNDEFINED = 0;

	/**
	 * C�digo para identifica��o de uma a��o de tecla do teclado.
	 */
	public static final int IV_KEY = 1;

	/**
	 * C�digo para identifica��o de uma a��o de bot�o do mouse.
	 */
	public static final int IV_BUTTON = 2;

	/**
	 * Vetor contendo o nome de todas as a��es dispon�veis.
	 */
	private static final String[] TYPE_NAME = new String[]
	{
		"UNDEFINED", "KEY", "BUTTON"
	};


	/**
	 * C�digo da a��o que foi detectada em um evento.
	 */
	private final int code;

	/**
	 * Qual a dura��o da a��o para que seja v�lida.
	 */
	private final int duration;

	/**
	 * Momento em que o evento da a��o foi criado.
	 */
	private final long initialize;

	/**
	 * Constr�i um novo evento acionado por a��es detectadas no JNI Input.
	 * @param type qual o g�nero da a��o que foi encontrada no sistema.
	 * @param code c�digo que indica o valor da a��o executada.
	 * @param duration quanto tempo a a��o dever� durar.
	 */

	public ActionEvent(int type, int code, int duration)
	{
		super(EV_INPUT, type);

		this.code = code;
		this.duration = duration;
		this.initialize = System.currentTimeMillis();
	}

	/**
	 * Procedimento que deve obter o valor que foi atribu�do a a��o de evento.
	 * @param expected valor que � esperado que essa a��o de evento possua.
	 * @return true se for o valor esperado ou false caso contr�rio,
	 * se for o esperado tamb�m ser� consumido para n�o ser usado de novo.
	 */

	public boolean getCode(int expected)
	{
		if (code != expected || isConsumed())
			return false;

		consume();

		return true;
	}

	/**
	 * Dever� verificar se essa a��o de evento ainda � v�lida ou n�o.
	 * Para ser v�lida n�o pode ter sido consumida e estar no intervalo da dura��o.
	 * @return true se ambas condi��es estiverem atendidas ou false caso contr�rio.
	 */

	public boolean isValid()
	{
		return !isConsumed() && !isHover();
	}

	/**
	 * Dever� verificar se essa a��o de evento ainda est� dentro do seu intervalo de dura��o.
	 * Utilizado por <code>isValid()</code> como condi��o para essa a��o ser v�lida.
	 * @return true se estiver dentro do tempo ou false caso contr�rio.
	 */

	public boolean isHover()
	{
		long current = System.currentTimeMillis();

		return current > initialize + duration;
	}

	@Override
	public String getIDString()
	{
		return "INPUT";
	}

	@Override
	public String getTypeString()
	{
		return TYPE_NAME[getType()];
	}

	@Override
	protected void toString(ObjectDescription description)
	{
		description.append("code", code);
	}
}
