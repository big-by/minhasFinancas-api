import React, { Component } from 'react'
import { withRouter } from 'react-router-dom'
import { PainelCentral } from '../components/PainelCentral'
import axios from 'axios';

//import UsuarioService from '../app/service/usuarioService'
//import { AuthContext } from '../main/provedorAutenticacao'

class Home extends Component {

	constructor() {
		super()
		this.state = {
			saldo: 0
		}
		//this.usuarioService = new UsuarioService();
	}

	componentDidMount() {
		const usuarioLogado = JSON.parse(
			localStorage.getItem('_usuario_logado')
		);
		const URL = `http://localhost:8080/api/usuarios/${usuarioLogado.id}/saldo`;

		axios.get(URL)
			.then(response => {
				this.setState({
					saldo: response.data
				})
			}).catch(error => {
				console.error(error.response)
			});
		/*const usuarioLogado = this.context.usuarioAutenticado

		this.usuarioService
				.obterSaldoPorUsuario(usuarioLogado.id)
				.then( response => {
						this.setState({ saldo: response.data})
				}).catch(error => {
						console.error(error.response)
				});*/
	}

	render() {
		return (
			<PainelCentral>
				<div className="jumbotron">
					<h1 className="display-3">Bem vindo!</h1>
					<p className="lead">Esse é seu sistema de finanças.</p>
					<p className="lead">Seu saldo para o mês atual é de R$ {this.state.saldo} </p>
					<hr className="my-4" />
					<p>E essa é sua área administrativa, utilize um dos menus ou botões abaixo para navegar pelo sistema.</p>
					<p className="lead">
						<a className="btn btn-primary btn-lg"
							href="/cadastro"
							role="button">
							<i className="pi pi-users">
								Cadastrar Usuário
							</i>

						</a>
						<a className="btn btn-danger btn-lg"
							href="/cadastro-lancamentos"
							role="button">
							<i className="pi pi-money-bill">
								Cadastrar Lançamento
							</i>
						</a>
					</p>
				</div>
			</PainelCentral>
		)
	}
}

//Home.contextType = AuthContext;

export default withRouter(Home)