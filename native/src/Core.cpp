
#include <windows.h>

#include "Core.h"
#include "KeyEvent.h"

HINSTANCE instance = NULL;			// Instância pra "Singleton"

JavaVM *jvm = NULL;					// Java Virtual Machine
DWORD hookThreadId = 0;				// Thread que foi detectada o uso da DLL

jobject keyboardHookObject = NULL;	// Objeto que será usado contendo o processo de eventos
jmethodID processKeyMethod = NULL;	// Método que será chamado para processar o evento

extern "C"

#define DISPATCH_KV(env, key, transition, type) env->CallVoidMethod(keyboardHookObject, processKeyMethod, parseKeyEvent(env, key, transition, type));

/**
 * Chamado no momento em que o sistema detectar a utilização dessa DLL.
 * @param hinstance referência da instância que utiliza a DLL.
 * @param reason qual a rasão na utilização da DLL.
 * @param reserved objeto reservado para outros afins.
 */
BOOL APIENTRY DllMain(HINSTANCE hinstance, DWORD reason, LPVOID reserved)
{
	switch (reason)
	{
		case DLL_PROCESS_ATTACH:
			instance = hinstance;
			break;
	}

	return TRUE;
}

/**
 * Procedimento chamado sempre que o sistema detectar um evento de teclado.
 * @param code código da tecla do qual foi detectada pelo evento.
 * @param wParam qual o tipo de evento que está sendo detectado.
 * @param lParam objeto contendo informações do evento.
 */
LRESULT CALLBACK LowLevelKeyboardProc(int code, WPARAM wParam, LPARAM lParam)
{
	JNIEnv* env;
	KBDLLHOOKSTRUCT* key = (KBDLLHOOKSTRUCT*) lParam;

	if (jvm->AttachCurrentThread((void **)&env, NULL) >= 0)
	{
		switch (wParam)
		{
			case WM_KEYDOWN:
			case WM_SYSKEYDOWN:
				DISPATCH_KV(env, key, true, KEY_TYPED);
				DISPATCH_KV(env, key, true, KEY_PRESSED);
				break;

			case WM_KEYUP:
			case WM_SYSKEYUP:
				DISPATCH_KV(env, key, false, KEY_RELEASED);
				break;
		}
	}

	return CallNextHookEx(NULL, code, wParam, lParam);
}

/**
 * Chamado sempre que for necessário fazer o registro de um novo serviço para teclado.
 * Esse serviço deverá ser salvo como um serviço de escuta para eventos do teclado.
 * @param env referência do softwre do qual está chamando esse procedimento.
 * @param obj referência do objeto no software que está tentando registrar.
 * @param listener referência do objeto que será a escuta dos eventos.
 */
JNIEXPORT void JNICALL Java_org_diverproject_jni_input_KeyboardPicker_register(JNIEnv *env, jobject obj)
{
	HHOOK hookHandle = SetWindowsHookEx(WH_KEYBOARD_LL, LowLevelKeyboardProc, instance, 0);

	if (hookHandle == NULL)
		return;

	keyboardHookObject = env->NewGlobalRef(obj);

	jclass cls = env->GetObjectClass(keyboardHookObject);
	processKeyMethod = env->GetMethodID(cls, "receiveKey", "(Lorg/diverproject/jni/input/KeyEvent;)V");

	env->GetJavaVM(&jvm);
	hookThreadId = GetCurrentThreadId();

	MSG message;

	while (GetMessage(&message, NULL, 0, 0))
	{
		TranslateMessage(&message);
		DispatchMessage(&message);
	}
}

/**
 * Chamado para desfazer o registro de um serviço para receber os eventos detecados do teclado.
 * Deverá interromper a thread do qual foi detecada como responsável pelo objeto de escuta.
 * @param env referência do softwre do qual está chamando esse procedimento.
 * @param obj referência do objeto no software que está tentando desfazer o registro.
 */
JNIEXPORT void JNICALL Java_org_diverproject_jni_input_KeyboardPicker_unregister(JNIEnv * env, jobject obj)
{
	if (hookThreadId == 0)
		return;

	PostThreadMessage(hookThreadId, WM_QUIT, 0, 0L);
}
