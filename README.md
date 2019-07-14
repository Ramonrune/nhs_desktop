# NHS DESKTOP

```
Projeto de Conclusão de Curso apresentado
como requisito parcial para obtenção do grau
Tecnólogo em Análise e Desenvolvimento de
Sistemas, pela Faculdade de Tecnologia de
Americana.
Time: 
RAMON LACAVA GUTIERREZ GONÇALES
LEONARDO MARTINS DE OLIVEIRA
NATÁLIA AKINA UESUGI
Orientador: Prof. Dr. Kleber de Oliveira Andrade
Área de concentração: Engenharia de Software
Americana, SP
2018


Este trabalho se trata de uma plataforma de saúde digital unificada, que permite melhor gestão de informações de saúde e processos inteligentes. 
A plataforma visa auxiliar o dia a dia das instituições de saúde e pacientes, buscando diminuir a quantidade de erros na área médica através de uma coleção consistente de dados do paciente, possibilitando que o sistema atue em qualquer instituição de saúde, e que tenha uma melhor eficiência e eficácia em atendimentos, sejam estes comuns ou de urgência e emergência.
O trabalho foi realizado em colaboração com a universidade de Durban, na África do Sul. 
A metodologia empregada durante o decorrer do trabalho foi o SCRUM, que visa a transparência, dinamicidade e agrega valor ao produto final. 
Foram desenvolvidos dois aplicativos para dispositivos móveis e um para computador, que realizam gestão de exames, diagnósticos, dados de saúde, medicamentos, instituições, médicos(as), dentre outros. 
Diversos requisitos foram coletados de forma dinâmica com as equipes sul africanas para possibilitar a integração dos sistemas tanto no Brasil quanto na África do Sul.
Todo o desenvolvimento do sistema se voltou para agregar valor aos processos e interfaces de usuário (se focando em facilidade de uso e experiencia de usuário).
Os resultados foram dois aplicativos publicados na Google Play e um sistema computadorizado, sendo que os três estão em fase de testes na África do Sul.
Conclui-se que o sistema poderá auxiliar muito no ambiente da saúde, facilitando a gestão, fornecendo processos inteligentes e uma maior agilidade no atendimento, permitindo com que pacientes possuam acesso a seus dados de saúde, e com que funcionários de saúde possuam uma maior facilidade e uma maior quantidade de dados relevantes para análise durante os atendimentos.
```


##### Palavras Chave: Saúde; Sistema; Internacionalização; Prontuário Médico; NFC; Arduino; Android; Java

## Especificações de pacotes
<p><a name="_Toc530233493"></a>Tabela - Especifica&ccedil;&atilde;o dos pacotes utilizados</p>
<table>
<tbody>
<tr>
<td width="302">
<p><strong>Nome do pacote</strong></p>
</td>
<td width="302">
<p><strong>Descri&ccedil;&atilde;o</strong></p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem</p>
</td>
<td width="302">
<p>Pacote base.</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.audittrail</p>
</td>
<td width="302">
<p>Recursos e telas utilizados pela trilha de auditoria.</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.errorlog</p>
</td>
<td width="302">
<p>Recursos e telas utilizados pelo log de erros.</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.healthinstitution</p>
</td>
<td width="302">
<p>Recursos e telas utilizados pelo gerenciamento e sele&ccedil;&atilde;o de institui&ccedil;&otilde;es de sa&uacute;de.</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.home</p>
</td>
<td width="302">
<p>Recursos, telas e estrutura principal do sistema.</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.notification</p>
</td>
<td width="302">
<p>Tela de notifica&ccedil;&atilde;o do sistema.</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.patient</p>
</td>
<td width="302">
<p>Tela para gerenciamento do paciente, exames, diagn&oacute;sticos, etc.</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.physician</p>
</td>
<td width="302">
<p>Tela para gest&atilde;o do(a) m&eacute;dico(a).</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.user</p>
</td>
<td width="302">
<p>Tela para gest&atilde;o de usu&aacute;rio</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.user.nurse</p>
</td>
<td width="302">
<p>Recursos para gest&atilde;o do(a) enfermeiro(a).</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.user.physician</p>
</td>
<td width="302">
<p>Recursos para gest&atilde;o do(a) m&eacute;dico(a).</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.user.specialization</p>
</td>
<td width="302">
<p>Recursos para lidar com especializa&ccedil;&otilde;es.</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.util</p>
</td>
<td width="302">
<p>Classes utilit&aacute;rias.</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.util.azure</p>
</td>
<td width="302">
<p>Classes utilit&aacute;rias para utilizar a Microsoft Azure.</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.util.component</p>
</td>
<td width="302">
<p>Classes de componentes gr&aacute;ficos desenvolvidos.</p>
</td>
</tr>
<tr>
<td width="302">
<p>com.healthsystem.util.dataprovider</p>
</td>
<td width="302">
<p>Classes fornecedoras de dados.</p>
</td>
</tr>
</tbody>
</table>
<p><strong>Fonte: Elaborado pelo autor</strong></p>


## Permissões
<p>Tabela - Tabela de permiss&otilde;es</p>
<table>
<tbody>
<tr>
<td width="245">
<p><strong>Perfil</strong></p>
</td>
<td width="359">
<p><strong>Telas</strong></p>
</td>
</tr>
<tr>
<td width="245">
<p>Administrador</p>
</td>
<td width="359">
<p>Painel, notifica&ccedil;&atilde;o, log de erro, log de auditoria, gerenciamento de institui&ccedil;&otilde;es de sa&uacute;de e usu&aacute;rio e altera&ccedil;&atilde;o de perfil.</p>
</td>
</tr>
<tr>
<td width="245">
<p>Administrador da institui&ccedil;&atilde;o de sa&uacute;de</p>
</td>
<td width="359">
<p>Painel, notifica&ccedil;&atilde;o, sele&ccedil;&atilde;o da institui&ccedil;&atilde;o de sa&uacute;de, gerenciamento de usu&aacute;rio e altera&ccedil;&atilde;o de perfil.</p>
</td>
</tr>
<tr>
<td width="245">
<p>M&eacute;dico</p>
</td>
<td width="359">
<p>Painel, notifica&ccedil;&atilde;o, sele&ccedil;&atilde;o da institui&ccedil;&atilde;o de sa&uacute;de, gerenciamento de pacientes, altera&ccedil;&atilde;o de perfil.</p>
</td>
</tr>
<tr>
<td width="245">
<p>Enfermeira</p>
</td>
<td width="359">
<p>Painel, notifica&ccedil;&atilde;o, sele&ccedil;&atilde;o da institui&ccedil;&atilde;o de sa&uacute;de, altera&ccedil;&atilde;o de perfil.</p>
</td>
</tr>
<tr>
<td width="245">
<p>Param&eacute;dico</p>
</td>
<td width="359">
<p>Painel, notifica&ccedil;&atilde;o, sele&ccedil;&atilde;o da institui&ccedil;&atilde;o de sa&uacute;de, altera&ccedil;&atilde;o de perfil.</p>
</td>
</tr>
<tr>
<td width="245">
<p>Paciente</p>
</td>
<td width="359">
<p>N&atilde;o possui acesso.</p>
</td>
</tr>
</tbody>
</table>
<p><strong>Fonte: Elaborado pelo autor</strong></p>

## Imagens
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/01.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/02.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/03.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/04.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/05.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/06.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/07.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/08.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/09.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/10.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/11.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/12.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/13.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/14.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/15.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/16.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/17.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/18.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/19.png'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/20.jpg'></p>
<p align="center"><img src='https://github.com/Ramonrune/nhs_desktop/blob/master/21.jpg'></p>



## Licença

    Copyright 2019 
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

