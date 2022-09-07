import Apiservice from "../apiservice";

class UsuarioService extends Apiservice {
    constructor() {
        super('api/usuarios');
    }

    autenticar(credenciais) {
        return this.post('/login', credenciais);
    }

    obterSaldoPorUsuario(id) {
        return this.get(`${id}/saldo`);
    }

    salvar(usuario) {
        return this.post('/', usuario);
    }

}

export default UsuarioService;