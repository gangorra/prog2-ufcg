package agenda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Interface com menus texto para manipular uma agenda de contatos.
 * 
 * @author nazarenoandrade
 *
 */
public class MainAgenda {

	public static void main(String[] args) {
		Agenda agenda = new Agenda();

		System.out.println("Carregando agenda inicial");
		try {
			/*
			 * Essa é a maneira de lidar com possíveis erros por falta do arquivo. 
			 */
			carregaAgenda("agenda_inicial.csv", agenda);
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo não encontrado: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Erro lendo arquivo: " + e.getMessage());
		}

		Scanner scanner = new Scanner(System.in);
		String escolha = "";
		while (true) {
			escolha = menu(scanner);
			comando(escolha, agenda, scanner);
		}

	}

	/**
	 * Exibe o menu e captura a escolha do/a usuário/a.
	 * 
	 * @param scanner Para captura da opção do usuário.
	 * @return O comando escolhido.
	 */
	private static String menu(Scanner scanner) {
		System.out.print(
				"\n---\nMENU\n" + 
						"(C)adastrar Contato\n" + 
						"(L)istar Contatos\n" + 
						"(E)xibir Contato\n" + 
						"(F)avorito\n" + 
						"(A)dicionar Favorito\n" + 
						"(S)air\n" + 
						"\n" + 
						"Opção> ");
		return scanner.nextLine().toUpperCase();
	}

	/**
	 * Interpreta a opção escolhida por quem está usando o sistema.
	 * 
	 * @param opcao   Opção digitada.
	 * @param agenda  A agenda que estamos manipulando.
	 * @param scanner Objeto scanner para o caso do comando precisar de mais input.
	 */
	private static void comando(String opcao, Agenda agenda, Scanner scanner) {
		switch (opcao) {
		case "C":
			cadastraContato(agenda, scanner);
			break;
		case "L":
			listaContatos(agenda);
			break;
		case "E":
			exibeContato(agenda, scanner);
			break;
		case "F":
			listaFavoritos(agenda);
			break;
		case "A":
			adicionarFavorito(agenda, scanner);
			break;
		case "S":
			sai();
			break;
		default:
			System.out.println("OPÇÃO INVÁLIDA!");
		}
	}

	/**
	 * Imprime lista de contatos da agenda.
	 * 
	 * @param agenda A agenda sendo manipulada.
	 */
	private static void listaContatos(Agenda agenda) {
		System.out.println("\nLista de contatos: ");
		Contato[] contatos = agenda.getContatos();
		for (int i = 0; i < contatos.length; i++) {
			if (contatos[i] != null) {
				System.out.println(formataContato(i, contatos[i]));
			} 
		}
	}

	/**
	 * Imprime os detalhes de um dos contatos da agenda. 
	 * 
	 * @param agenda A agenda.
	 * @param scanner Scanner para capturar qual contato.
	 */
	private static void exibeContato(Agenda agenda, Scanner scanner) {
		System.out.print("Contato> ");
		int posicao = Integer.parseInt(scanner.nextLine());
		Contato contato = agenda.getContato(posicao);

		if (contato == null) {
			System.out.println("\nPOSIÇÃO INVÁLIDA!");
			return;
		}

		//se estiver nos favoritos
		if (agenda.ehFavorito(contato)) {
			System.out.print("❤️ " + contato.toString());
		} else {
			System.out.println("\n" + contato.toString());
		}
	}

	//lista favoritos
	private static void listaFavoritos(Agenda agenda) {
		Contato[] favoritos = agenda.getFavoritos();
		for (int i = 0; i < favoritos.length; i++) {
			if (favoritos[i] != null) {
				System.out.println("\n" + formataContato(i, favoritos[i]));
			}
		}
	}

	//add favoritos
	private static void adicionarFavorito(Agenda agenda, Scanner scanner) {
		System.out.print("Contato> ");
		int posicaoContato = Integer.parseInt(scanner.nextLine());		//posicao do contato
		System.out.print("Posição> ");
		int posicaoFavorito = Integer.parseInt(scanner.nextLine());	//posicao de favorito

		agenda.cadastraFavorito(posicaoContato, posicaoFavorito);
		System.out.println("CONTATO FAVORITADO NA POSIÇÃO " + posicaoFavorito + "!");
	}

	/**
	 * Formata um contato para impressão na interface. 
	 * 
	 * @param posicao A posição do contato (que é exibida)/
	 * @param contato O contato a ser impresso.
	 * @return A String formatada.
	 */
	private static String formataContato(int posicao, Contato contato) {
		return posicao + " - " + contato.getUsuarioCompleto();
	}

	/**
	 * Cadastra um contato na agenda. 
	 * 
	 * @param agenda A agenda.
	 * @param scanner Scanner para pedir informações do contato.
	 */
	private static void cadastraContato(Agenda agenda, Scanner scanner) {
		System.out.print("\nPosição> ");
		int posicao = Integer.parseInt(scanner.nextLine());

		if (posicao <= 0 || posicao > 100) {
			System.out.println("\nPOSIÇÃO INVÁLIDA!");
			return;
		}

		System.out.print("Nome> ");
		String nome = scanner.nextLine();
		if (nome == null || nome.equals("")) {System.out.println("\nCONTATO INVALIDO!"); return;}

		System.out.print("Sobrenome> ");
		String sobrenome = scanner.nextLine();

		//verificando se já existe
		Contato[] contatos = agenda.getContatos();	//chamando clone de contatos
		for (int i = 0; i < contatos.length; i++) {
			if(contatos[i] != null) {
				String nomeContato = contatos[i].getNome();
				String sobrenomeContato = contatos[i].getSobrenome();

				if (nome.equals(nomeContato) && sobrenome.equals(sobrenomeContato)) {
					System.out.println("\nCONTATO JÁ CADASTRADO!");
					return;
				}
			}
		}
		
		
		System.out.print("Telefone> ");
		String telefone = scanner.nextLine();
		if (telefone == null || telefone.equals("")) {System.out.println("\nCONTATO INVALIDO!"); return;}

		agenda.cadastraContato(posicao, nome, sobrenome, telefone);
		System.out.println("\nCADASTRO REALIZADO!");
	}

	/**
	 * Sai da aplicação.
	 */
	private static void sai() {
		System.out.println("\nVlw flw o/");
		System.exit(0);
	}

	/**
	 * Lê uma agenda de um arquivo csv. 
	 * 
	 * @param arquivoContatos O caminho para o arquivo.
	 * @param agenda A agenda que deve ser populada com os dados. 
	 * @throws IOException Caso o arquivo não exista ou não possa ser lido.
	 */
	private static void carregaAgenda(String arquivoContatos, Agenda agenda) throws FileNotFoundException, IOException {
		LeitorDeAgenda leitor = new LeitorDeAgenda();
		
		int carregados =  leitor.carregaContatos(arquivoContatos, agenda);
		System.out.println("Carregamos " + carregados + " registros.");
	}
}
