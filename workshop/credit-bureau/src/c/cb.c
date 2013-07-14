/**
 * Este aplicacion representa a una entidad crediticia la cual
 * concentra todos los creditos otorgados a los clientes de
 * diferentes entidades.
 */

#include "library.h"

char *findRFC(char cadena[], int socket)
{
  FILE *file = openFile("r"); 
  char line[MAX];
  char *lines_to_send = malloc(MAX_TO_SEND);
  char values[MAX][MAX];
  char line_backup[MAX];

  int i = 0;
  int RFC_position = 0;

  bzero(lines_to_send,MAX_TO_SEND);
  while(!feof(file))
  {
    bzero(line,MAX);
    bzero(line_backup,MAX);
    fgets(line, MAX, file);
    //fscanf(file,"%[^\n]",line);
    //fseek(file,2,SEEK_CUR);
//     printf("Esta es la linea numero %i: %s-\n",i,line);
    strcpy(line_backup,line);
    splitLine(line,values,SEPARATOR_FILE);
    if(i == 0)
    {
      RFC_position = getColumnPosition(values,RFC);
    }
    else
    {
      if(!strcmp(values[RFC_position],cadena))
      {
	/*
	 * This part has a problem, how am I gonna avoid that the
	 * line has the \n at the end??? ... This is doing that 
	 * the final package has a \n between the lines to send
	 */
	printf("Encontrado %s = %s\n",values[RFC_position],cadena);
	//strcat(lines_to_send,PACKAGE_START);
// 	strcat(lines_to_send,line_backup);
// 	strcat(lines_to_send,"\b\b\b");
//  	strcat(lines_to_send,(char *)itoa(SEPARATOR_FILE)); //(char*)SEPARATOR_FILE
	printf("Paquete a enviar: %s\n",line_backup);
	int n = sendMessage(socket,line_backup,MAX);
	checkForErrors(n);
      }
    }
    i++;
  }
  fclose(file);
  return "Envío Terminado";
}

char* changeStatus(char mensaje[])
{
  FILE *file = openFile("r+");
  char line[MAX];
  char values[MAX][MAX];
  char RFC_string[MAX];
  char DATE_string[MAX];
  int i = 0;
  int RFC_position = 0;
  int DATE_position = 0;
  
  printf("El mensaje original es: %s\n",mensaje);
  splitLine(mensaje,values,SEPARATOR_FILE);
  strcpy(RFC_string,values[0]);
  strcpy(DATE_string,values[1]);
  printf("El RFC es: %s\n",RFC_string);
  printf("La FECHA es: %s\n",DATE_string);
  bzero(values[0],MAX);
  bzero(values[1],MAX);

  while(!feof(file))
  {
    bzero(line,MAX);
    fgets(line, MAX, file);
//     printf("Esta es la linea numero %i: %s-\n",i,line);
    splitLine(line,values,SEPARATOR_FILE);
    if(i == 0)
    {
      RFC_position = getColumnPosition(values,RFC);
      DATE_position = getColumnPosition(values,DATE);
      printf("RFC_position: %i\nDATE_position: %i\n",RFC_position,DATE_position);
    }
    else
    {
      printf("Valor en [5] -%s-\n",values[DATE_position]);
      printf("Cheking ... Getting in to a comparation\n%s = %s\n%s = %s\n",values[RFC_position],RFC_string,values[DATE_position],DATE_string);
      if(!strcmp(values[RFC_position],RFC_string) && !strcmp(values[DATE_position],DATE_string))
      {
	/*
	 * Be sure that the last line of the 'file' has a "\n" 
	 * it means that after the last register has to be a 
	 * blank line or the fseek(...,-3,...) is going to fail
	 */ 
	fseek(file,-3,SEEK_CUR);
	fputc(CLOSE_LOAN,file);
	fclose(file);
	return "The Loan has been closed correctly";
      }
    }
    i++;
  }
  fclose(file);
  return "The Loan has NOT been closed";
}

char* insertData(char mensaje[])
{
  FILE *file = openFile("r+");
  fseek(file,0,SEEK_END);
  strcat(mensaje,"\n");
  fputs(mensaje,file);
  fclose(file);
  return "Se ha insertado un nuevo registro en el archivo";
}

void doprocessing (int sock)
{
    int n;
    char buffer[256];
    char answer_to_client[MAX_TO_SEND];
    char values[MAX][MAX];
    int operation;
    char client_message[MAX];

    bzero(answer_to_client,MAX_TO_SEND);
    bzero(buffer,256);
    n = read(sock,buffer,255);
    if (n < 0)
    {
        perror("ERROR reading from socket\n");
        exit(1);
    }
    printf("Here is the message: %s\n",buffer);
    splitLine(buffer,values,SEPARATOR_OPERATION);
    operation = atoi(values[0]);
    strcpy(client_message,values[1]);
//     printf("El valor de BUFFER ES: -%s-\n",values[1]);
    switch(operation)
    {
      case 1:
	      {
		char *message = findRFC(client_message,sock);
		strcpy(answer_to_client,message);
	      }
	break;
      case 2:
	      {
		char *message = insertData(client_message);
		strcpy(answer_to_client,message);
	      }
	break;
      case 3:
	      {
		char *message = changeStatus(client_message);
		strcpy(answer_to_client,message);
	      }
	break;
      default:
	      {	
		printf("There is an error with the client's message\nReturning the message to the client ...\n");
		char *message = "Error: You don't have the correct message syntaxis";
		strcpy(answer_to_client,message);
	      }
	break;
    }
    
    //n = write(sock,"I got your message\n",18);
    //n = write(sock,answer_to_client,MAX);
    n = sendMessage(sock,answer_to_client,MAX);
    checkForErrors(n);
}

void start()
{
   /* los descriptores de Sockets (que son archivos) */
   int Socket_1, Socket_2; 

   /*
    * para la información de la dirección del servidor formato de la estructura:
      struct in_addr
      {
	unsigned long int s_addr;
      };
      struct sockaddr_in
      {
	int sin_family;
	unsigned short int sin_port;
	struct in_addr sin_addr;
      };
   */
   struct sockaddr_in server;
   struct sockaddr_in client;

   unsigned int sin_size;

   /* Para almacenar los process ID de los hijos*/
   pid_t pid;

   /* A continuación la llamada a socket() para crear el socket 1 */
   if ((Socket_1=socket(AF_INET, SOCK_STREAM, 0)) == -1 ) 
   {
      printf("error en socket()\n");
      exit(-1);
   }
   /*Se llena la estructura del servidor con el tipo conexion a usar
    * y el puerto, con un previo Casting (htoINADDR_ANY coloca nuestra dirección IP automáticamentens) de la constante PORT
    * INADDR_ANY coloca nuestra dirección IP automáticamente
    */
   server.sin_family = AF_INET;
   server.sin_port = htons(PORT);
   server.sin_addr.s_addr = INADDR_ANY;
   /* escribimos ceros en el reto de la estructura */
   memset(&(server.sin_zero), '0', 8);

   /* A continuación la llamada a bind() para asignar el puerto al Socket */
   if(bind(Socket_1,(struct sockaddr*)&server, sizeof(struct sockaddr))==-1) 
   {
      printf("error en bind() \n");
      exit(-1);
   }

   /* llamada a listen() Para poner el socket a escuchar las peticiones*/
   if(listen(Socket_1,BACKLOG) == -1) 
   { 
      printf("error en listen()\n");
      exit(-1);
   }

   /*Se inicia un ciclo infinito*/
   while(1) {
      sin_size=sizeof(struct sockaddr_in);
      /* 
       * A continuación la llamada a accept() para aceptar las conexiones entrantes
       * Un vez que se crea un conexion se crea un nuevo socket y el original que escucha
       * se queda inalterado
       */
      if ((Socket_2 = accept(Socket_1,(struct sockaddr *)&client, &sin_size))==-1) 
      {
         printf("error en accept()\n");
         exit(-1);
      }

      /* Aqui se mostrará la IP del cliente */
      printf("Se obtuvo una conexión desde %s\n", inet_ntoa(client.sin_addr) );
      /* Se enviará el mensaje de bienvenida al cliente */
      //send(Socket_2,"Bienvenido a mi servidor",20,0);
      /*
       * Se crean procesos hijo, si regresa menos de 0, hubo un error, 
       * si regresa 0 es el proceso hijo (un clon exacto del proceso
       * entero) y si no aplica ninguno, entonces por logica es el padre
       * y debe cerrar ese segundo socket para recibir peticiones nuevas
       */
      pid = fork();
      if (pid < 0) {
          perror("ERROR on fork\n");
           exit(-1);
      }
      if (pid == 0)
      {
          /* This is the client process */
          close(Socket_1);
          doprocessing(Socket_2);
	  close(Socket_2);
	  printf("CERRANDO EL SOCKET DEL CLIENTE\n");
          exit(0);
      }
      else
      {
	  //close(Socket_1);
          close(Socket_2);
      }
   } /* end while */
}

int main()
{
  /*
  char cadena[MAX][MAX] ;
  strcpy(cadena[0],"Hola Mundo");
  strcpy(cadena[1],"Hello World");
  strcpy(cadena[2],"Whots up");
  printf("En la funcion el 2-2 es: %c\n",cadena[2][2]);
  splitFile(cadena);
  printf("En la funcion el 2-2 es: %c\n",cadena[2][2]);
  */
  start();

  return (0);
}

