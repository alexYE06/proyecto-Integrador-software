import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { ChoferAuthService } from '../services/chofer-auth.service';

/**
 * Guard funcional canActivate: protege la cabina móvil del Chofer en ruta.
 * Si no hay una sesión activa de conductor validada con DNI, redirige a /login-chofer.
 */
export const choferGuard: CanActivateFn = (route, state) => {
  const choferAuth = inject(ChoferAuthService);
  const router = inject(Router);

  if (choferAuth.estaAutenticado()) {
    return true;
  }

  router.navigate(['/login-chofer'], {
    queryParams: { returnUrl: state.url }
  });
  return false;
};
