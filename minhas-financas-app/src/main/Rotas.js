import React from "react";
import { Route, Switch, BrowserRouter } from 'react-router-dom';
import CadastroUsuarios from "../views/CadastroUsuarios";
import Home from "../views/Home";
import Login from "../views/Login";

function Rotas() {
    return (
        <BrowserRouter>
            <Switch>
                <Route exact path="/" component={Login} />
                <Route exact path="/cadastro" component={CadastroUsuarios} />
                <Route exact path="/home" component={Home} />
            </Switch>
        </BrowserRouter>
    )
}

export default Rotas;