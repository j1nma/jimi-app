import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import {Card, CardBody, CardHeader, Col, Row, Table} from 'reactstrap';
import UserRestClient from '../../http/clients/UserRestClient';
import Reactotron from 'reactotron-react-js';
import {connect} from "react-redux";
import Spinner from "reactstrap/es/Spinner";
import Button from "reactstrap/es/Button";
import Modal from "reactstrap/es/Modal";
import ModalHeader from "reactstrap/es/ModalHeader";
import {AvField, AvForm} from "availity-reactstrap-validation";
import ModalBody from "reactstrap/es/ModalBody";
import InputGroupAddon from "reactstrap/es/InputGroupAddon";
import InputGroupText from "reactstrap/es/InputGroupText";
import ModalFooter from "reactstrap/es/ModalFooter";
import CardFooter from "reactstrap/es/CardFooter";
import ButtonGroup from "reactstrap/es/ButtonGroup";
import i18n from '../../i18n';

function UserRow(props) {
  const user = props.user;
  const userLink = `/users/${user.id}`;

  return (
    <tr>
      <th scope="row">{user.id}</th>
      <td><Link to={userLink}>{user.username}</Link></td>
    </tr>
  )
}

class Users extends Component {

  usersClient;

  pageSize = 5;

  constructor(props) {
    super(props);
    this.usersClient = new UserRestClient(props);
    this.state = {
      users: [],
      loading: true,
      modal: false,
      form: {username: '', password: '', confirmPassword: '', error: false, nameError: false},
      links: {next: null, last: null, prev: null, first: null, page: null}
    };
    this.toggle = this.toggle.bind(this);
    this.newUser = this.newUser.bind(this);
    this.updateList = this.updateList.bind(this);
    this.handleValidSubmit = this.handleValidSubmit.bind(this);
    this.handleInvalidSubmit = this.handleInvalidSubmit.bind(this);
    this.getPaginationLinks = this.getPaginationLinks.bind(this);
  }

  toggle() {
    this.setState(prevState => ({
      modal: !prevState.modal,
      form: {username: '', password: '', confirmPassword: ''},
    }));
  }

  newUser() {
    this.setState({loading: true});
    this.usersClient.create(this.state.form.username, this.state.form.password, this.state.form.confirmPassword)
      .then(() => this.updateList())
      .then(() => this.toggle())
      .then(() => this.setState({loading: false}))
      .catch((error) => {
        Reactotron.error("Failed to create user");

        let form = {...this.state.form};
        form.error = true;
        if (error.response.status === 409) form.nameError = true;
        this.setState({loading: false, form: form});
      });
  }

  updateList(page) {
    page = page ? page : 1;
    return this.usersClient.get(page, this.pageSize)
      .then((val) => {
        let links = {...this.state.links};
        links.next = val.data.links.next;
        links.last = val.data.links.last;
        links.prev = val.data.links.prev;
        links.first = val.data.links.first;
        links.page = val.data.links.page;
        this.setState({users: val.data.users, links: links});
      }).catch((error) => {
        Reactotron.error("Failed to retrieve users", error);
      });
  }

  componentDidMount() {
    this.updateList().finally(() => this.setState({loading: false}));
  }

  handleValidSubmit(event, values) {
    let form = {...this.state.form};
    form.error = false;
    form.nameError = false;
    form.username = values.username;
    form.password = values.password;
    form.confirmPassword = values.confirmPassword;
    this.setState({form: form});
    this.newUser()
  }

  handleInvalidSubmit(event, errors, values) {
    let form = {...this.state.form};
    form.error = true;
    form.nameError = false;
    form.username = values.username;
    form.password = values.password;
    form.confirmPassword = values.confirmPassword;
    this.setState({form: form});
  }

  getPaginationLinks() {
    return this.state.links.first !== this.state.links.last ? (
      <ButtonGroup style={{'width': '100%', marginBottom: '10px'}}>
        {this.state.links.prev ?
          <Button onClick={() => this.updateList(this.state.links.prev)}>
            <i className="fa fa-chevron-left"/></Button> : ''}
        {this.state.links.next ?
          <Button onClick={() => this.updateList(this.state.links.next)}>
            <i className="fa fa-chevron-right"/></Button> : ''}
      </ButtonGroup>
    ) : '';
  }

  render() {
    if (this.state.loading === true) return <Spinner style={{width: '3rem', height: '3rem'}}/>;

    return (
      <div className="animated fadeIn">
        <Row>
          <Col xl={12}>
            <Card>
              <CardHeader>
                <i className="fa fa-align-justify"/> {i18n.t('navMenu.users')} <small className="text-muted"/>
                <Button onClick={this.toggle} style={{'float': 'right'}} color="primary" className="px-4">
                  <i className="fa fa-plus-circle"/> {i18n.t('users.single')}
                </Button>
              </CardHeader>
              <CardBody>
                <Table responsive hover>
                  <thead>
                  <tr>
                    <th scope="col">ID</th>
                    <th scope="col">{i18n.t('users.name')}</th>
                  </tr>
                  </thead>
                  <tbody>
                  {this.state.users.map((user, index) =>
                    <UserRow key={index} user={user}/>
                  )}
                  </tbody>
                </Table>
              </CardBody>
              <CardFooter>
                {this.getPaginationLinks()}
              </CardFooter>
            </Card>
          </Col>
        </Row>

        <Modal isOpen={this.state.modal} toggle={this.toggle} className={this.props.className}>
          <ModalHeader toggle={this.toggle}>
            {i18n.t('users.new')}
          </ModalHeader>
          <AvForm onValidSubmit={this.handleValidSubmit} onInvalidSubmit={this.handleInvalidSubmit}>
            <ModalBody>
              <AvField name="username" label={i18n.t('users.name')} type="text" validate={{
                required: {value: true, errorMessage: i18n.t('users.validation.requiredName')},
                pattern: {
                  value: '[a-zA-Z0-9]+',
                  errorMessage: i18n.t('users.validation.pattern')
                },
                minLength: {value: 6, errorMessage: i18n.t('users.validation.minLength')},
                maxLength: {value: 40, errorMessage: i18n.t('users.validation.maxLength')}
              }}/>
              {this.state.form.nameError ? (
                <InputGroupAddon addonType="append">
                  <InputGroupText>
                    {i18n.t('users.validation.existentUsername')}
                  </InputGroupText>
                </InputGroupAddon>) : ''}
              <AvField name="password" label={i18n.t('users.password')} type="password" validate={{
                required: {value: true, errorMessage: i18n.t('users.validation.requiredPassword')},
                minLength: {value: 6, errorMessage: i18n.t('users.validation.minLength')},
                maxLength: {value: 40, errorMessage: i18n.t('users.validation.maxLength')}
              }}/>
              <AvField name="confirmPassword" label={i18n.t('users.confirmPassword')} type="password" validate={{
                required: {value: true, errorMessage: i18n.t('users.validation.requiredPassword')},
                minLength: {value: 6, errorMessage: i18n.t('users.validation.minLength')},
                maxLength: {value: 40, errorMessage: i18n.t('users.validation.maxLength')},
                match: {value: 'password', errorMessage: i18n.t('users.validation.passwordMatch')}
              }}/>
            </ModalBody>
            <ModalFooter>
              <Button color="primary" className="px-4" block>{i18n.t('global.save')}</Button>
              <Button color="secondary" onClick={this.toggle}>{i18n.t('global.cancel')}</Button>
            </ModalFooter>
          </AvForm>
        </Modal>
      </div>
    )

  }
}

const mapStateToProps = state => {
  return {
    token: state.authentication.token
  };
};

export default connect(mapStateToProps)(Users);
