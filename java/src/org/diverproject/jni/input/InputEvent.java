package org.diverproject.jni.input;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Evento de Entrada</h1>
 *
 * <p>Na biblioteca JNI Input existem diversos tipos de eventos que podem ser encontrados.
 * Essa classe visa determinar algumas propriedades e funções que são iguais em todos.
 * Para tal foi definido algumas propriedades que todos os eventos possuem:</p>
 *
 * <p>A identificação irá determinar o gênero do evento, em outras palavra a "classe de origem".
 * O tipo é uma forma de identificação, porém para determinar o que o evento deve fazer.
 * Por último o consumo, a ideia dessa propriedade é que ao consumir o evento ele seja perdido.</p>
 *
 * <p>Consumos de eventos devem podem ser feitos em qualquer momento e não podem ser desfeitos.
 * Para que o evento seja de fato perdido ele deverá ser feito pelo serviço que o está usando.
 * Internamente irá apenas atribuir uma valor a uma das propriedades indicando que foi consumido.</p>
 *
 * @author Andrew
 */

public abstract class InputEvent
{
	/**
	 * Código para eventos de entrada.
	 */
	public static final int EV_INPUT = 1;

	/**
	 * Código para eventos de teclado.
	 */
	public static final int EV_KEYBOARD = 2;


	/**
	 * Identificação do gênero de evento a qual este pertence.
	 */
	private final int id;

	/**
	 * Tipo de ação que o evento está executando.
	 */
	private final int type;

	/**
	 * Determina se o evento já foi consumido.
	 */
	private boolean consumed;

	/**
	 * Constrói um novo evento sendo necessário definir o seguinte:
	 * @param id identificação do gênero a qual o evento pertence.
	 * @param type código da ação que o evento está executando.
	 */

	public InputEvent(int id, int type)
	{
		this.id = id;
		this.type = type;
	}

	/**
	 * Código de identificação do evento é usado apenas para visualização.
	 * Pode ser usado para identificar o gênero do evento e efetuar cast.
	 * @return aquisição da identificação do gênero desse evento.
	 */

	public int getID()
	{
		return id;
	}

	/**
	 * Tipo do evento irá determinar qual a ação que o evento irá executar.
	 * Para cada tipo uma ação diferente deve ser executada no sistema.
	 * As ações variam de acordo com o gênero do evento em questão.
	 * @return aquisição do tipo de ação a ser executada pelo evento.
	 */

	public int getType()
	{
		return type;
	}

	/**
	 * Deve ser chamado apenas quando for necessário consumir o evento.
	 * Uma vez que o evento tenha sido consumido não poderá mais ser desfeito.
	 * Sistemas internos do JNI Input que usem esses eventos ignoram consumidos.
	 */

	public void consume()
	{
		consumed = true;
	}

	/**
	 * Irá verificar se o evento em questão já foi consumindo durante a sua utilização.
	 * @return true se tiver sido consumido ou false caso contrário.
	 */

	public boolean isConsumed()
	{
		return consumed;
	}

	/**
	 * Usado apenas para uma identificação visual do evento, sendo chamado por <code>toString()</code>.
	 * @return aquisição do nome da identificação do gênero desse evento.
	 */

	public abstract String getIDString();

	/**
	 * Usado apenas para uma identificação visual do evento, sendo chamado por <code>toString()</code>.
	 * @return aquisição do nome da ação que o evento deverá executar.
	 */

	public abstract String getTypeString();

	/**
	 * Procedimento chamado pra definir propriedades ao <code>toString()</code> desse evento.
	 * Quando chamado terá todos os outros atributos já definidos, sendo necessário
	 * apenas definir os atributos específicos do evento que está herdado InputEvent.
	 * @param description objeto contendo a descrição inicial do evento.
	 */

	protected abstract void toString(ObjectDescription description);

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("id", getIDString());
		description.append("type", getTypeString());
		description.append("consumed", consumed);

		toString(description);

		return description.toString();
	}
}
