import React from "react";
import { Route, Switch, BrowserRouter } from 'react-router-dom';
import CadastroUsuarios from "../views/CadastroUsuarios";
import ConsultaLancamentos from "../views/ConsultaLancamentos";
import Home from "../views/Home";
import Login from "../views/Login";

function Rotas() {
    return (
        <BrowserRouter>
            <Switch>
                <Route exact path="/" component={Home} />
                <Route path="/login" component={Login} />
                <Route path="/cadastro" component={CadastroUsuarios} />
                <Route path="/home" component={Home} />
                <Route path="/consulta-lancamentos" component={ConsultaLancamentos} />
            </Switch>
        </BrowserRouter>
    )
}

export default Rotas;