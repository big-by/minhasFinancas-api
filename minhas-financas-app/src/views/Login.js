import React, { Component } from "react";
import Card from "../components/Card";
import { Col12, Col6 } from "../components/Grid/Col";
import FormGroup from "../components/FormGroup";
import Row from "../components/Grid/Row";

class Login extends Component {
	
	constructor(){
		super();
		this.state = {
			email: null,
			senha: null
		}
		//this.service = new UsuarioService();
	}
	
	onChangeEmail = (event) => {
		this.setState({
			email: event.target.value
		});
	}
	
	onChangeSenha = (event) => {
		this.setState({
			senha: event.target.value
		});
	}
	
	/*entrar = () => {
		this.service.autenticar({
			email: this.state.email,
			senha: this.state.senha
		}).then( response => {
			this.context.iniciarSessao(response.data)
			this.props.history.push('/home')
		}).catch( erro => {
			mensagemErro(erro.response.data)
		})
	}*/
	
	prepareCadastrar = () => {
		this.props.history.push('/cadastro-usuarios')
	}
	
	render(){
		return (
			<Row>
				<Col6>
					<div className="bs-docs-section">
						<Card title="Login">
							<Row>
								<Col12>
									<div className="bs-component">
										<fieldset>
											<FormGroup label="Email: *" htmlFor="exampleInputEmail1">
												<input type="email" 
													value={this.state.email}
													onChange={this.onChangeEmail}
													className="form-control" 
													id="exampleInputEmail1" 
													aria-describedby="emailHelp" 
													placeholder="Digite o Email" />
											</FormGroup>
											<FormGroup label="Senha: *" htmlFor="exampleInputPassword1">
												<input type="password" 
													value={this.state.senha}
													onChange={this.onChangeSenha}
													className="form-control" 
													id="exampleInputPassword1" 
													placeholder="Password" />
											</FormGroup>
											<button 
												onClick={this.entrar} 
												className="btn btn-success">
												<i className="pi pi-sign-in">
													Entrar
												</i>
											</button>
											<button 
												onClick={this.prepareCadastrar} 
												className="btn btn-danger">
												<i className="pi pi-plus">
													Cadastrar
												</i> 
											</button>
										</fieldset>
									</div>
								</Col12>
							</Row>
						</Card>
					</div>
				</Col6>
			</Row>
			)
			
		}
	}
	
	export default Login