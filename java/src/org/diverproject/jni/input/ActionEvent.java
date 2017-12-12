package org.diverproject.jni.input;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Evento de Ação</h1>
 *
 * <p>Eventos desse tipo são usados (recomendado) pelos despachantes para um evento detectado.
 * Por exemplo, uma tecla clicada não deve ficar na lista de teclas clicadas para sempre.
 * Usado esse evento que deverá garantir a sua existência no sistema por um período.</p>
 *
 * <p>Essas ações devem possuir um valor que será usado para identificação a ação executada.
 * Quanto a duração não há um padrão para tal, deve ser definido de acordo com a seu uso.
 * Possui também todas as informações de um Evento de Entrada como outro qualquer./<p>
 *
 * @see InputEvent
 *
 * @author Andrew
 */

public class ActionEvent extends InputEvent
{
	/**
	 * Código para identificação de uma ação não identificada.
	 */
	public static final int IV_UNDEFINED = 0;

	/**
	 * Código para identificação de uma ação de tecla do teclado.
	 */
	public static final int IV_KEY = 1;

	/**
	 * Código para identificação de uma ação de botão do mouse.
	 */
	public static final int IV_BUTTON = 2;

	/**
	 * Vetor contendo o nome de todas as ações disponíveis.
	 */
	private static final String[] TYPE_NAME = new String[]
	{
		"UNDEFINED", "KEY", "BUTTON"
	};


	/**
	 * Código da ação que foi detectada em um evento.
	 */
	private final int code;

	/**
	 * Qual a duração da ação para que seja válida.
	 */
	private final int duration;

	/**
	 * Momento em que o evento da ação foi criado.
	 */
	private final long initialize;

	/**
	 * Constrói um novo evento acionado por ações detectadas no JNI Input.
	 * @param type qual o gênero da ação que foi encontrada no sistema.
	 * @param code código que indica o valor da ação executada.
	 * @param duration quanto tempo a ação deverá durar.
	 */

	public ActionEvent(int type, int code, int duration)
	{
		super(EV_INPUT, type);

		this.code = code;
		this.duration = duration;
		this.initialize = System.currentTimeMillis();
	}

	/**
	 * Procedimento que deve obter o valor que foi atribuído a ação de evento.
	 * @param expected valor que é esperado que essa ação de evento possua.
	 * @return true se for o valor esperado ou false caso contrário,
	 * se for o esperado também será consumido para não ser usado de novo.
	 */

	public boolean getCode(int expected)
	{
		if (code != expected || isConsumed())
			return false;

		consume();

		return true;
	}

	/**
	 * Deverá verificar se essa ação de evento ainda é válida ou não.
	 * Para ser válida não pode ter sido consumida e estar no intervalo da duração.
	 * @return true se ambas condições estiverem atendidas ou false caso contrário.
	 */

	public boolean isValid()
	{
		return !isConsumed() && !isHover();
	}

	/**
	 * Deverá verificar se essa ação de evento ainda está dentro do seu intervalo de duração.
	 * Utilizado por <code>isValid()</code> como condição para essa ação ser válida.
	 * @return true se estiver dentro do tempo ou false caso contrário.
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
