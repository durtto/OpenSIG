package br.com.opensig.empresa.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;

public class ListagemFornecedor extends AListagemEntidade<EmpFornecedor> {

	public ListagemFornecedor(IFormulario<EmpFornecedor> formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		listaCampos.add(new IntegerFieldDef("empFornecedorId"));
		listaColunas.add(new ColumnConfig(OpenSigCore.i18n.txtCod(), "empFornecedorId", 50, true));
		super.configurar();

		campos = new RecordDef(listaCampos.toArray(new FieldDef[] {}));
		modelos = new ColumnModel(listaColunas.toArray(new BaseColumnConfig[] {}));
		super.inicializar();
	}
	
	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();
		
		// compra
		String strCompra = FabricaComando.getInstancia().getComandoCompleto("ComandoCompra");
		SisFuncao compra = UtilClient.getFuncaoPermitida(strCompra);
		MenuItem itemCompra = gerarFuncao(compra, "empFornecedor.empFornecedorId", "empFornecedorId");
		if (itemCompra != null) {
			mnuContexto.addItem(itemCompra);
		}
		
		// produto
		String strProduto = FabricaComando.getInstancia().getComandoCompleto("ComandoProduto");
		SisFuncao produto = UtilClient.getFuncaoPermitida(strProduto);
		MenuItem itemProduto = gerarFuncao(produto, "empFornecedor.empFornecedorId", "empFornecedorId");
		if (itemProduto != null) {
			mnuContexto.addItem(itemProduto);
		}
		
		// pagar
		String strPagar = FabricaComando.getInstancia().getComandoCompleto("ComandoPagar");
		SisFuncao pagar = UtilClient.getFuncaoPermitida(strPagar);
		MenuItem itemPagar = gerarFuncao(pagar, "empEntidade.empEntidadeId", "empEntidade.empEntidadeId");
		if (itemPagar != null) {
			mnuContexto.addItem(itemPagar);
		}
		
		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
