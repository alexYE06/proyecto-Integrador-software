import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard funcional canActivate: protege las rutas de la Central de Operaciones.
 * Si el usuario no ha iniciado sesión, lo redirige al login con returnUrl.
 */
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.estaAutenticado()) {
    return true;
  }

  // Redirigir al login y guardar la URL que intentaba visitar
  router.navigate(['/login-central'], {
    queryParams: { returnUrl: state.url }
  });
  return false;
};
