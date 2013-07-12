/**
 * Este aplicacion representa a una entidad crediticia la cual
 * concentra todos los creditos otorgados a los clientes de
 * diferentes entidades.
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>

#define PORT 3550 /* El puerto que será abierto */
#define BACKLOG 2 /* El número de conexiones permitidas */
#define MAX 600
#define MAX_TO_SEND 600
#define SEPARATOR "|"
#define RFC "RFC"
#define DATE "DATE"
#define CLOSE_LOAN 'N'
#define DATA_FILE "Loans.txt"


/* 
 * This function gives you the number column 
 * of the RFC in the File
 * so you can change the position without affect
 * the code
 */
int getDatePosition(char (*encabezado)[MAX])
{
  int i = 0;
  while(i < MAX)
  {
    if(!strcmp(encabezado[i],DATE))
    {
      break;
    }
    i++;
  }
  return i;
}

/* 
 * This function gives you the number column 
 * of the RFC in the File
 * so you can change the position without affect
 * the code
 */
int getRfcPosition(char (*encabezado)[MAX])
{
  int i = 0;
  while(i < MAX)
  {
    if(!strcmp(encabezado[i],RFC))
    {
      break;
    }
    i++;
  }
  return i;
}


/*
 * This function split the given line (from the DATA_FILE)
 * and put it into a bidimensional array
 */
void splitLine(char line[],char (*values)[MAX]) 
{
  char *split_word;
  int i = 0;
  /*This code split the received line and put it
   * into the bidimensional array
   */
  while ((split_word = strsep(&line, SEPARATOR)) != NULL)
  {
    strcpy(values[i],split_word);
    i++;
  }
}

/*This function open the file
 * it get a parameter to define
 * how to open the file (read, write, etc)
 */
FILE* openFile(char permission[])
{
  /*It's missing a excpetion handler*/
  FILE *file;
  file = fopen(DATA_FILE,permission);
  fseek(file,0,SEEK_SET);
  return file;
}
 
void findRFC(char cadena[])
{
  FILE *file = openFile("r"); 
  char line[MAX];
  char lines_to_send[MAX_TO_SEND];
  char values[MAX][MAX];
  char line_backup[MAX];
  
  int i = 0;
  int RFC_position = 0;
  
  bzero(lines_to_send,MAX_TO_SEND);
  while(!feof(file))
  {
    bzero(line,MAX);
    fgets(line, MAX, file);
    printf("Esta es la linea numero %i: %s-\n",i,line);
    strcpy(line_backup,line);
    splitLine(line,values);
    if(i == 0)
    {
      RFC_position = getRfcPosition(values);
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
	printf("Encontrao %s = %s\n",values[RFC_position],cadena);
	strcat(lines_to_send,line_backup);
	strcat(lines_to_send,SEPARATOR);
	printf("Paquete a enviar: %s\n",lines_to_send);
      }
    }
    i++;
  }
}

void changeStatus(char mensaje[])
{
  FILE *file = openFile("r+");
  //Encontrar el RFC
  char line[MAX];
//   char lines_to_send[MAX_TO_SEND];
  char values[MAX][MAX];
//   char line_backup[MAX];
  char RFC_string[MAX];
  char DATE_string[MAX];
  int i = 0;
  int RFC_position = 0;
  int DATE_position = 0;
  
  splitLine(mensaje,values);
  strcpy(RFC_string,values[0]);
  strcpy(DATE_string,values[1]);
  bzero(values[0],MAX);
  bzero(values[1],MAX);
//   bzero(lines_to_send,MAX_TO_SEND);
  
  while(!feof(file))
  {
    bzero(line,MAX);
    fgets(line, MAX, file);
    printf("Esta es la linea numero %i: %s-\n",i,line);
    //strcpy(line_backup,line);
    splitLine(line,values);
    if(i == 0)
    {
      RFC_position = getRfcPosition(values);
      DATE_position = getDatePosition(values);
    }
    else
    {
      if(!strcmp(values[RFC_position],RFC_string) && !strcmp(values[DATE_position],DATE_string))
      {
	fseek(file,-3,SEEK_CUR);
	fputc(CLOSE_LOAN,file);
	break;
      }
    }
    i++;
  }
}

void insertData(char mensaje[])
{
  FILE *file = openFile("r+");
  fseek(file,0,SEEK_END);
  strcat(mensaje,"\n");
  fputs(mensaje,file);
  printf("Se ha insertado un nuevo registro en el archivo");
}

void doprocessing (int sock)
{
    int n;
    char buffer[256];
    bzero(buffer,256);
    n = read(sock,buffer,255);
    //findRFC(buffer);
    //insertData(buffer);
    changeStatus(buffer);
    if (n < 0)
    {
        perror("ERROR reading from socket\n");
        exit(1);
    }
    printf("Here is the message: %s\n",buffer);
    n = write(sock,"I got your message\n",18);
    if (n < 0)
    {
        perror("ERROR writing to socket\n");
        exit(1);
    }
}


void start()
{
   /* los descriptores de Sockets (que son archivos) */
   int Socket_1, Socket_2; 

   /* para la información de la dirección del servidor formato de la estructura:
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
      printf("Ya se creo el Fork\n");
      if (pid < 0) {
          perror("ERROR on fork\n");
           exit(-1);
      }
      if (pid == 0)
      {
          /* This is the client process */
	  printf("Entrando a doprocessing\n");
          close(Socket_1);
          doprocessing(Socket_2);
	  close(Socket_2);
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

