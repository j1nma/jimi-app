import React, {Component, Suspense} from 'react';
import {Redirect, Route, Switch} from 'react-router-dom';
import {Container} from 'reactstrap';

import {
  AppAside,
  AppBreadcrumb,
  AppFooter,
  AppHeader,
  AppSidebar,
  AppSidebarFooter,
  AppSidebarForm,
  AppSidebarHeader,
  AppSidebarMinimizer,
  AppSidebarNav,
} from '@coreui/react';
// sidebar nav config
import navigation from '../../_nav';
// routes config
import routes from '../../routes';

const DefaultAside = React.lazy(() => import('./DefaultAside'));
const DefaultFooter = React.lazy(() => import('./DefaultFooter'));
const DefaultHeader = React.lazy(() => import('./DefaultHeader'));

class DefaultLayout extends Component {

  navOptions = [];
  allowedRoutes = [];

  constructor(props) {
    super(props);

    // handle navigation by ROLES
    navigation.items.forEach( (value) => {
      if(value.roles.filter(value => -1 !== props.store.getState().authentication.roles.indexOf(value)).length > 0) {
        // add value to navOptions
        this.navOptions.push(value);
      }
    });

    // handle routes by ROLES
    routes.forEach( (value) => {
      if(value.roles.filter(value => -1 !== props.store.getState().authentication.roles.indexOf(value)).length > 0) {
        // add value to navOptions
        this.allowedRoutes.push(value);
      }
    });
  }

  loading = () => <div className="animated fadeIn pt-1 text-center">Loading...</div>;

  signOut = (e) => {
    // e.preventDefault();
    // console.log(this.props.history.push);
    // this.props.history.push('/login')
  };

  render() {
    return (
      <div className="app">
        <AppHeader fixed>
          <Suspense fallback={this.loading()}>
            <DefaultHeader onLogout={e => this.signOut(e)} history={this.props.history}/>
          </Suspense>
        </AppHeader>
        <div className="app-body">
          <AppSidebar fixed display="lg">
            <AppSidebarHeader/>
            <AppSidebarForm/>
            <Suspense>
              <AppSidebarNav navConfig={{items: this.navOptions}} {...this.props} />
            </Suspense>
            <AppSidebarFooter/>
            <AppSidebarMinimizer/>
          </AppSidebar>
          <main className="main">
            <AppBreadcrumb appRoutes={routes}/>
            <Container fluid>
              <Suspense fallback={this.loading()}>
                <Switch>
                  {this.allowedRoutes.map((route, idx) => {
                    return route.component ? (
                      <Route
                        key={idx}
                        path={route.path}
                        exact={route.exact}
                        name={route.name}
                        render={props => (
                          <route.component {...props} />
                        )}/>
                    ) : (null);
                  })}
                  <Redirect exact={true} from={'/'} to="/tables"/>
                  <Redirect to="/404"/>
                </Switch>
              </Suspense>
            </Container>
          </main>
          <AppAside fixed>
            <Suspense fallback={this.loading()}>
              <DefaultAside/>
            </Suspense>
          </AppAside>
        </div>
        <AppFooter>
          <Suspense fallback={this.loading()}>
            <DefaultFooter/>
          </Suspense>
        </AppFooter>
      </div>
    );
  }
}

export default DefaultLayout;
