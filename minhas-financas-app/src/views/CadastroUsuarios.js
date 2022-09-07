import React, { Component } from "react";
import Card from "../components/Card";
import FormGroup from "../components/FormGroup";
import { Col12 } from "../components/Grid/Col";
import Row from "../components/Grid/Row";
import { PainelCentral } from "../components/PainelCentral";
import { withRouter } from 'react-router-dom';
import UsuarioService from "../app/service/usuarioService";


class CadastroUsuarios extends Component {
	constructor() {
		super();
		this.state = {
			nome: null,
			email: null,
			senha: null,
			senhaRepetida: null,
			mensagem: null,
		}
		this.service = new UsuarioService()
	}

	onChange = (event, campo) => {
		this.setState({
			[campo]: event.target.value
		})
	}

	cadastrar = () => {

		const { nome, email, senha, senhaRepeticao } = this.state
		const usuario = { nome, email, senha, senhaRepeticao }

		/*try {
			this.service.validar(usuario);
		} catch (erro) {
			const msgs = erro.mensagens;
			msgs.forEach(msg => mensagemErro(msg));
			return false;
		}*/

		this.service.salvar({ email, nome, senha })
			.then(response => {
				this.setState({
					mensagem: 'Usuário cadastrado com sucesso! Faça o login para acessar o sistema.'
				});
				this.props.history.push('/login')
			}).catch(error => {
				this.setState({
					mensagem: error.response.data
				});
			})
	}

	cancelar = () => {
		this.props.history.push('/login')
	}

	render() {
		return (
			<PainelCentral>
				<Card title="Cadastro de Usuários">
					<Row>
						<Col12>
							<div className="bs-component">
								<FormGroup label="Nome: *" htmlFor="inputNome">
									<input type="text"
										id="inputNome"
										className="form-control"
										name="nome"
										onChange={(e) => this.onChange(e, 'nome')} />
								</FormGroup>
								<FormGroup label="Email: *" htmlFor="inputEmail">
									<input type="email"
										id="inputEmail"
										className="form-control"
										name="email"
										onChange={(e) => this.onChange(e, 'email')} />
								</FormGroup>
								<FormGroup label="Senha: *" htmlFor="inputSenha">
									<input type="password"
										id="inputSenha"
										className="form-control"
										name="senha"
										onChange={(e) => this.onChange(e, 'senha')} />
								</FormGroup>
								<FormGroup label="Repita a Senha: *" htmlFor="inputRepitaSenha">
									<input type="password"
										id="inputRepitaSenha"
										className="form-control"
										name="senha"
										onChange={(e) => this.onChange(e, 'senhaRepetida')} />
								</FormGroup>
								<button onClick={this.cadastrar} type="button" className="btn btn-success">
									<i className="pi pi-save"></i> Salvar
								</button>
								<button onClick={this.cancelar} type="button" className="btn btn-danger">
									<i className="pi pi-times"></i> Cancelar
								</button>
							</div>
						</Col12>
					</Row>
				</Card>
			</PainelCentral>
		)
	}
}

export default withRouter(CadastroUsuarios)