import { Component } from "react";
import { withRouter } from 'react-router-dom';
import Card from "../components/Card";
import FormGroup from "../components/FormGroup";
import { Col12 } from "../components/Grid/Col";
import Row from "../components/Grid/Row";
import SelectMenu from "../components/SelectMenu";
import LancamentoService from "../app/service/lancamentoService";
import TabelaLancamento from "../components/Lancamentos/TabelaLancamento";


class ConsultaLancamentos extends Component {
    constructor() {
        super();
        this.state = {
            ano: '',
            mes: '',
            tipo: '',
            descricao: '',
            showConfirmDialog: false,
            lancamentoDeletar: {},
            lancamentos: []
        }
        this.service = new LancamentoService();
    }


    render() {

        const meses = this.service.obterListaMeses();

        const tipos = this.service.obterListaTipos();

        return (
            <Card title="Consulta lançamentos">
                <Row>
                    <Col12>
                        <div className="bs-component">
                            <FormGroup htmlFor="inputAno" label="Ano: *">
                                <input type="text"
                                    className="form-control"
                                    id="inputAno"
                                    value={this.state.ano}
                                    onChange={e => this.setState({ ano: e.target.value })}
                                    placeholder="Digite o Ano" />
                            </FormGroup>

                            <FormGroup htmlFor="inputMes" label="Mês: ">
                                <SelectMenu id="inputMes"
                                    value={this.state.mes}
                                    onChange={e => this.setState({ mes: e.target.value })}
                                    className="form-control"
                                    lista={meses} />
                            </FormGroup>

                            <FormGroup htmlFor="inputDesc" label="Descrição: ">
                                <input type="text"
                                    className="form-control"
                                    id="inputDesc"
                                    value={this.state.descricao}
                                    onChange={e => this.setState({ descricao: e.target.value })}
                                    placeholder="Digite a descrição" />
                            </FormGroup>

                            <FormGroup htmlFor="inputTipo" label="Tipo Lançamento: ">
                                <SelectMenu id="inputTipo"
                                    value={this.state.tipo}
                                    onChange={e => this.setState({ tipo: e.target.value })}
                                    className="form-control"
                                    lista={tipos} />
                            </FormGroup>

                            <button onClick={this.buscar}
                                type="button"
                                className="btn btn-success">
                                <i className="pi pi-search">Buscar</i>
                            </button>
                            <button onClick={this.preparaFormularioCadastro}
                                type="button"
                                className="btn btn-danger">
                                <i className="pi pi-plus"></i> Cadastrar
                            </button>
                        </div>
                    </Col12>
                </Row>
                <Row>
                    <Col12>
                        <div className="bs-component">
                            <TabelaLancamento
                                lancamentos={this.state.lancamentos}
                                deleteAction={this.abrirConfirmacao}
                                editAction={this.editar}
                                alterarStatus={this.alterarStatus} />
                        </div>
                    </Col12>
                </Row>
            </Card>
        )
    }

}

export default withRouter(ConsultaLancamentos)