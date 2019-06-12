/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.iury06;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author iurya
 */
@Component("relatorioControle")
public class RelatorioControle {

    @Autowired
    DataSource dataSource;

    public Integer getTotalClientes() {
        Integer tot = null;
        try {

            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("SELECT COUNT(*) FROM cliente");
            executeQuery.next();
            tot = executeQuery.getInt(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tot;
    }

    public Integer getTotalLojas() {
        Integer tot = null;
        try {

            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("SELECT COUNT(*) FROM loja");
            executeQuery.next();
            tot = executeQuery.getInt(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tot;
    }

    public int getQuantidadeVendas() {
        Integer tot = null;
        try {

            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("SELECT COUNT(*) FROM vendas");
            executeQuery.next();
            tot = executeQuery.getInt(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tot;
    }

    public Double getTotalVendas() {
        double tot = 0.0;
        try {

            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("SELECT SUM(valor_vendas)FROM vendas");
            executeQuery.next();
            tot = executeQuery.getDouble(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tot;

    }

    public String getTotalVenda() {
        String tot = "";
        try {

            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("SELECT id_cliente, nome, SUM(valor_vendas) AS Total_comprado  \n"
                            + "FROM vendas inner join cliente on vendas.id_cliente = cliente.id \n"
                            + "inner join tempo on vendas.id_tempo = tempo.id where data_venda BETWEEN'\"15/03/2019\"' AND '\"20/05/2019\"'\n"
                            + "GROUP BY id_cliente, nome  \n"
                            + "ORDER BY id_cliente;");

            executeQuery.next();
            tot = executeQuery.getString(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tot;

    }
    
        /////////Total valor por clientes/ano\\\\\\\\
    
    String ano;
    String semestre;
    List<String[]> resultado = new ArrayList<>();

    public List<String[]> getResultado() {
        
        
        resultado.clear();
        try {

            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("SELECT id_cliente,cliente.cod, nome, cliente.cpf, SUM(valor_vendas) AS Total_comprado  \n" +
"FROM vendas inner join cliente on vendas.id_cliente = cliente.id \n" +
"inner join tempo on vendas.id_tempo = tempo.id where ano='"+ano+"' AND semestre='"+semestre+"'\n" +
"GROUP BY id_cliente,cod, cpf , nome  \n" +
"ORDER BY id_cliente;");
            
            
           while( executeQuery.next()){
            resultado.add(new String[]{executeQuery.getString(2), executeQuery.getString(3),executeQuery.getString(4),executeQuery.getString(5)});
         
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultado;
        
        
    }
    
    
    
              //////////Quantidade total de cada um dos produtos vendidos por tempo\\\\\\\\\\\

 
    List<String[]> resultado1 = new ArrayList<>();

    public List<String[]> getResultado1() {
        
        
        resultado1.clear();
        try {

            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("SELECT id_produto, produto.codigo, descricao, SUM(qtde_produto) AS Total_Vendido \n" +
"FROM vendas inner join produto on vendas.id_produto = produto.id \n" +
"inner join tempo on vendas.id_tempo = tempo.id where data_venda BETWEEN'"+dataInicial+"' AND '"+dataFinal+"'\n" +
"GROUP BY id_produto, descricao,codigo\n" +
"ORDER BY codigo;");
            
            
           while( executeQuery.next()){
            resultado1.add(new String[]{executeQuery.getString(2), executeQuery.getString(3),executeQuery.getString(4)});
         
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultado1;
        
        
    }

            /////////Total valor vendas por loja por semestre\\\\\\\\\
    
    
    String semestre2;
    List<String[]> resultado2 = new ArrayList<>();

    public List<String[]> getResultado2() {
        
        
        resultado2.clear();
        try {

            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("SELECT id_loja, loja.Cnpj, loja.nome, SUM(valor_vendas) AS Total_vendido  \n" +
"FROM vendas inner join loja on vendas.id_loja = loja.id \n" +
"inner join tempo on vendas.id_tempo = tempo.id where semestre = '"+semestre2+"'\n" +
"GROUP BY id_loja, cnpj, nome \n" +
"ORDER BY id_loja;");
            
            
           while( executeQuery.next()){
            resultado2.add(new String[]{executeQuery.getString(2), executeQuery.getString(3), executeQuery.getString(4)});
         
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultado2;
        
        
    }

    
    ///////// Quantidade total de vendas por produto e por lojas


    
    
    List<String[]> resultado3 = new ArrayList<>();

    public List<String[]> getResultado3() {
        
        
        resultado3.clear();
        try {

            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("SELECT id_loja, id_produto, loja.cnpj, produto.codigo as codigo_produto, produto.descricao, SUM(qtde_produto) AS qtde_vendido  \n" +
"FROM vendas inner join loja on vendas.id_loja = loja.id \n" +
"inner join produto on vendas.id_produto = produto.id \n" +
"GROUP BY id_loja, id_produto, cnpj, codigo, descricao \n" +
"ORDER BY id_loja;");
            
            
           while( executeQuery.next()){
            resultado3.add(new String[]{executeQuery.getString(3), executeQuery.getString(4),executeQuery.getString(5), executeQuery.getString(6)});
         
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultado3;
        
        
    }
    
      ///////// (valor) por região.


    
    
    List<String[]> resultado4 = new ArrayList<>();

    public List<String[]> getResultado4() {
        
        
        resultado4.clear();
        try {

            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("SELECT regiao, SUM(valor_vendas) AS Total_vendido  \n" +
"FROM vendas inner join localidade on vendas.id_localidade = localidade.id \n" +
"GROUP BY  regiao  \n" +
"ORDER BY regiao ; ");
            
            
           while( executeQuery.next()){
            resultado4.add(new String[]{executeQuery.getString(1), executeQuery.getString(2)});
         
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultado4;
        
        
    }

    
    

    //// exportando CSV
    List<String[]> ArquivoJson = new ArrayList<>();

    public List<String[]> getArquivoJson() {
        
        
        ArquivoJson.clear();
        try {

            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("copy (SELECT codigo, descricao, categoria FROM produto) to 'C:/temp/produto.csv' with csv header;");
          
            
                    
           while( executeQuery.next()){
            ArquivoJson.add(new String[]{executeQuery.getString(1), executeQuery.getString(2),executeQuery.getString(3)});
         
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ArquivoJson;
    }
    
    
    //////////////////ordenação/////////
    
        List<String[]> resulBuble = new ArrayList<>();
        
    public void Execute() throws IOException, SQLException{
        
        
    List<Integer> listaCod = new ArrayList();
    List<String> listaDesc = new ArrayList();
    List<String> listaCateg = new ArrayList();
    
    
    
    FileWriter arquivo = new FileWriter("C:/temp/produto.json");
    PrintWriter copArquivo = new PrintWriter(arquivo); 
    
    
    
        ResultSet executeQuery = dataSource.getConnection()
                .createStatement().executeQuery("SELECT codigo, descricao, categoria FROM produto");
          
            
                    
           while( executeQuery.next()){
            listaCod.add(executeQuery.getInt("codigo"));
            listaDesc.add(executeQuery.getString("descricao"));
            listaCateg.add(executeQuery.getString("categoria"));
           }
           
           int codigo[];
           String descricao[];
           String categoria[];
           
           codigo = new int [listaCod.size()]; // crio um vetor com o tamanho da lista
           descricao = new String [listaDesc.size()];
           categoria = new String [listaCateg.size()];
           
           for(int i = 0; i < listaCod.size(); i++){
               
            codigo[i] = listaCod.get(i);
            descricao[i] = listaDesc.get(i);
            categoria[i] = listaCateg.get(i);
           }
           
           ///////////Insertion Sort\\\\\\\\\\\\\\

           
           for (int i = 0; i < codigo.length; i++) {
            int auxCod = codigo[i];
            String auxDesc = descricao[i];
            String auxCat = categoria[i];
            
            int j = i - 1;
            while (j >= 0 && codigo[j] >= auxCod) {
                codigo[j + 1] = codigo[j];
                descricao [j + 1] = descricao[j];
                categoria[j + 1] = categoria[j];
                
                j--;
            }
            codigo[j + 1] = auxCod;
            descricao[j + 1] = auxDesc;
            categoria[j + 1] = auxCat;
        }
  
           /////////////////\\\\\\\\\\\\\\\\\
 
             for(int i =0; i < codigo.length; i++){
                 resulBuble.add(new String[]{Integer.toString(codigo[i]), descricao[i], categoria[i]});
    
}
              /////conversão Json\\\\\
             ObjectMapper conversor = new ObjectMapper();
             String str = conversor.writeValueAsString(resulBuble);
             
            copArquivo.print(str);
            arquivo.close();
            
            System.out.println(str);
            
     
}
    

    
    public String getSemestre2() {
        return semestre2;
    }

    public void setSemestre2(String semestre2) {
        this.semestre2 = semestre2;
    }

    public void setResultado(List<String[]> resultado) {
        this.resultado = resultado;
    }

    public void consultarVendasLojaSemestre() {
        
        
    }

    public List<String[]> getResulBuble() {
        return resulBuble;
    }

    public void setResulBuble(List<String[]> resulBuble) {
        this.resulBuble = resulBuble;
    }


    
    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
    
       String dataInicial;
    String dataFinal;

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

}

