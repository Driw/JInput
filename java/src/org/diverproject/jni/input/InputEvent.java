package org.diverproject.jni.input;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Evento de Entrada</h1>
 *
 * <p>Na biblioteca JNI Input existem diversos tipos de eventos que podem ser encontrados.
 * Essa classe visa determinar algumas propriedades e fun��es que s�o iguais em todos.
 * Para tal foi definido algumas propriedades que todos os eventos possuem:</p>
 *
 * <p>A identifica��o ir� determinar o g�nero do evento, em outras palavra a "classe de origem".
 * O tipo � uma forma de identifica��o, por�m para determinar o que o evento deve fazer.
 * Por �ltimo o consumo, a ideia dessa propriedade � que ao consumir o evento ele seja perdido.</p>
 *
 * <p>Consumos de eventos devem podem ser feitos em qualquer momento e n�o podem ser desfeitos.
 * Para que o evento seja de fato perdido ele dever� ser feito pelo servi�o que o est� usando.
 * Internamente ir� apenas atribuir uma valor a uma das propriedades indicando que foi consumido.</p>
 *
 * @author Andrew
 */

public abstract class InputEvent
{
	/**
	 * C�digo para eventos de entrada.
	 */
	public static final int EV_INPUT = 1;

	/**
	 * C�digo para eventos de teclado.
	 */
	public static final int EV_KEYBOARD = 2;


	/**
	 * Identifica��o do g�nero de evento a qual este pertence.
	 */
	private final int id;

	/**
	 * Tipo de a��o que o evento est� executando.
	 */
	private final int type;

	/**
	 * Determina se o evento j� foi consumido.
	 */
	private boolean consumed;

	/**
	 * Constr�i um novo evento sendo necess�rio definir o seguinte:
	 * @param id identifica��o do g�nero a qual o evento pertence.
	 * @param type c�digo da a��o que o evento est� executando.
	 */

	public InputEvent(int id, int type)
	{
		this.id = id;
		this.type = type;
	}

	/**
	 * C�digo de identifica��o do evento � usado apenas para visualiza��o.
	 * Pode ser usado para identificar o g�nero do evento e efetuar cast.
	 * @return aquisi��o da identifica��o do g�nero desse evento.
	 */

	public int getID()
	{
		return id;
	}

	/**
	 * Tipo do evento ir� determinar qual a a��o que o evento ir� executar.
	 * Para cada tipo uma a��o diferente deve ser executada no sistema.
	 * As a��es variam de acordo com o g�nero do evento em quest�o.
	 * @return aquisi��o do tipo de a��o a ser executada pelo evento.
	 */

	public int getType()
	{
		return type;
	}

	/**
	 * Deve ser chamado apenas quando for necess�rio consumir o evento.
	 * Uma vez que o evento tenha sido consumido n�o poder� mais ser desfeito.
	 * Sistemas internos do JNI Input que usem esses eventos ignoram consumidos.
	 */

	public void consume()
	{
		consumed = true;
	}

	/**
	 * Ir� verificar se o evento em quest�o j� foi consumindo durante a sua utiliza��o.
	 * @return true se tiver sido consumido ou false caso contr�rio.
	 */

	public boolean isConsumed()
	{
		return consumed;
	}

	/**
	 * Usado apenas para uma identifica��o visual do evento, sendo chamado por <code>toString()</code>.
	 * @return aquisi��o do nome da identifica��o do g�nero desse evento.
	 */

	public abstract String getIDString();

	/**
	 * Usado apenas para uma identifica��o visual do evento, sendo chamado por <code>toString()</code>.
	 * @return aquisi��o do nome da a��o que o evento dever� executar.
	 */

	public abstract String getTypeString();

	/**
	 * Procedimento chamado pra definir propriedades ao <code>toString()</code> desse evento.
	 * Quando chamado ter� todos os outros atributos j� definidos, sendo necess�rio
	 * apenas definir os atributos espec�ficos do evento que est� herdado InputEvent.
	 * @param description objeto contendo a descri��o inicial do evento.
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
